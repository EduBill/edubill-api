package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.dto.user.SignUpRequestDto;
import com.edubill.edubillApi.dto.user.UserDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.repository.UserRepository;
import com.edubill.edubillApi.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final VerificationRepository verificationRepository;

    public VerificationResponseDto sendVerificationNumber(String phoneNumber) {
        // 휴대폰 번호 형식 체크
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("휴대폰 번호의 양식과 맞지 않습니다.");
        }

        // 인증번호 생성 및 저장
        final String verificationNumber = generateRandomNumber();
        final String requestId = UUID.randomUUID().toString();

        verificationRepository.put(requestId, verificationNumber);

        // 인증번호와 고유 요청 ID를 응답
        // 실제로는 해당 전화번호를 key 값으로 sms전송
        return new VerificationResponseDto(verificationNumber, requestId);
    }



    public String verifyNumber(String InputNumber, String requestId) {
        String verificationNumber = verificationRepository.get(requestId);

        // requestId에 대한 검증 번호가 존재하는지 확인
        if (verificationNumber == null) {
            throw new NoSuchElementException("해당요청에 대한 인증번호가 존재하지 않습니다. : " + requestId);
        }
        // 6자리 코드 같을 경우 인증
        boolean isVerify= verificationNumber.equals(InputNumber);
        if (!isVerify) {
            throw new IllegalArgumentException("인증번호가 맞지 않습니다. (InputNumber: " + InputNumber + ")");
        }
        return "인증 완료 되었습니다.";

    }

    // 구현 필요
    public UserDto signIn(String phoneNumber, String requestId) {
        return null;
    }

    @Transactional
    public UserDto signUp(SignUpRequestDto signUpRequestDto) {
        String phoneNumber = signUpRequestDto.getPhoneNumber();
        String userName = signUpRequestDto.getUserName();
        String requestId = signUpRequestDto.getRequestId();

        User user = new User(phoneNumber, userName, requestId);
        userRepository.save(user);

        UserDto userDto = UserDto.toDto(user);

        return userDto;
    }

    // 사용자 가입 여부 조회
    public Boolean isExistsUser(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    // 인증번호 생성 (6자리)
    private static String generateRandomNumber() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);
        return String.valueOf(randomNumber);
    }

    // 휴대폰 번호 형식 체크 메소드
    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^01(?:0|1|[6-9])(\\d{3,4})(\\d{4})$";
        return phoneNumber.matches(regex);
    }
}