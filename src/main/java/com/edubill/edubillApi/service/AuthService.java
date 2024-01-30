package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.domain.UserRole;
import com.edubill.edubillApi.dto.user.JoinRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.exception.UserAlreadyExistsException;
import com.edubill.edubillApi.repository.UserRepository;
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
    private final HashMap<String, String> verificationMap = new HashMap<>();


    public VerificationResponseDto sendVerificationNumber(String phoneNumber) {

        // 인증번호 생성 및 저장
        String verificationNumber = generateRandomNumber();
        String requestId = UUID.randomUUID().toString();

        verificationMap.put(requestId, verificationNumber);

        // 인증번호와 고유 요청 ID를 응답
        // 실제로는 해당 전화번호를 key 값으로 sms전송
        return new VerificationResponseDto(verificationNumber, requestId);
    }


    public String verifyNumber(String enteredNumber, String requestId) {
        String verificationNumber = verificationMap.get(requestId);

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

//    public User login(String phoneNumber, String requestId) {
//        Boolean hasUser = checkDuplicateUser(phoneNumber, requestId);
//        return hasUser;
//    }

    @Transactional
    public User join(JoinRequestDto joinRequestDto) {
        String phoneNumber = joinRequestDto.getPhoneNumber();
        String userName = joinRequestDto.getUserName();
        UserRole userRole = joinRequestDto.getUserRole();
        String requestId = joinRequestDto.getRequestId();

        if (checkDuplicateUser(phoneNumber)) {
            throw new UserAlreadyExistsException();
        }
        User user = new User(phoneNumber, userName, userRole, requestId);
        userRepository.save(user);
        return user;
    }

    // 인증번호 생성 (6자리)
    private static String generateRandomNumber() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    // 사용자 가입 여부 조회
    public Boolean checkDuplicateUser(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}
