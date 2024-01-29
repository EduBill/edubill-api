package com.edubill.edubillApi.service;
import com.edubill.edubillApi.domain.VerificationCodeEntity;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.dto.verification.VerifyCodeResponseDto;
import com.edubill.edubillApi.repository.UserRepository;
import com.edubill.edubillApi.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {


    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;

    public VerificationResponseDto sendVerificationCode(String phoneNumber) {
        // 인증번호 생성 및 저장
        String verificationCode = generateRandomCode();
        String requestId = UUID.randomUUID().toString();

        // 데이터베이스에 저장
        VerificationCodeEntity verificationCodeEntity = new VerificationCodeEntity(requestId, phoneNumber, verificationCode);
        verificationCodeRepository.save(verificationCodeEntity);

        // 인증번호와 고유 요청 ID를 응답
        return new VerificationResponseDto(verificationCode, requestId);
    }

    public void verifyCode(String enteredCode, String requestId) {
        // 저장된 인증번호 조회
        VerificationCodeEntity verificationCodeEntity = verificationCodeRepository.findById(requestId).orElse(null);

        if (verificationCodeEntity != null) {
            // 6자리 코드 같을 경우 인증
            boolean isCodeMatched = verificationCodeEntity.getVerificationCode().equals(enteredCode);
            if (!isCodeMatched) {
                throw new IllegalArgumentException("Verification code does not match(enteredCode :" + enteredCode + ")");
            }

        } else {
            throw new NoSuchElementException("VerificationCodeEntity not found for requestId: " + requestId);
        }
    }

    public CheckUserResponse checkUser(String phoneNumber) {
        // 사용자 가입 여부 조회
        boolean hasUser = userRepository.existsByPhoneNumber(phoneNumber);

        // 가입 여부를 응답
        return new CheckUserResponse(hasUser);
    }

    public LoginResponse login(String phoneNumber) {
        // 사용자 인증 및 세션 관리 등의 로직이 들어갈 것
        boolean success = userRepository.existsByPhoneNumber(phoneNumber);
        String errorCode = success ? null : "USER_NOT_FOUND";

        // 로그인 결과와 에러 코드를 응답
        return new LoginResponse(success, errorCode);
    }

    public RegisterResponse register(String phoneNumber, String name, String role) {
        // 사용자 등록
        UserEntity userEntity = new UserEntity(phoneNumber, name, role);
        userRepository.save(userEntity);

        // 회원가입 성공을 응답
        return new RegisterResponse(true, null);
    }

    // 인증번호 생성 메서드 (6자리)
    private static String generateRandomCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
