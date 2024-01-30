package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.domain.UserRole;
import com.edubill.edubillApi.dto.user.JoinRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.exception.UserAlreadyExistsException;
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
        String verificationNumber = generateRandomNumber();
        String requestId = UUID.randomUUID().toString();

        verificationRepository.put(requestId, verificationNumber);

        // 인증번호와 고유 요청 ID를 응답
        // 실제로는 해당 전화번호를 key 값으로 sms전송
        return new VerificationResponseDto(verificationNumber, requestId);
    }



    public String verifyNumber(String enteredNumber, String requestId) {
        String verificationNumber = verificationRepository.get(requestId);

        // requestId에 대한 검증 번호가 존재하는지 확인
        if (verificationNumber == null) {
            throw new NoSuchElementException("VerificationNumber not found for requestId: " + requestId);
        }
        // 6자리 코드 같을 경우 인증
        boolean isVerify= verificationNumber.equals(enteredNumber);
        if (!isVerify) {
            throw new IllegalArgumentException("Verification Number does not match (enteredNumber: " + enteredNumber + ")");
        }
        return "인증 완료 되었습니다.";

    }

    public User signIn(String phoneNumber, String requestId) {

    }

    @Transactional
    public User signUp(JoinRequestDto joinRequestDto) {
        String phoneNumber = joinRequestDto.getPhoneNumber();
        String userName = joinRequestDto.getUserName();
        UserRole userRole = joinRequestDto.getUserRole();
        String requestId = joinRequestDto.getRequestId();

        User user = new User(phoneNumber, userName, userRole, requestId);
        userRepository.save(user);
        return user;
    }

    // 사용자 가입 여부 조회
    public Boolean isExistsUser(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    // 인증번호 생성 (6자리)
    private static String generateRandomNumber() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    // 휴대폰 번호 형식 체크 메소드
    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^01(?:0|1|[6-9])(\\d{3,4})(\\d{4})$";
        return phoneNumber.matches(regex);
    }
}
