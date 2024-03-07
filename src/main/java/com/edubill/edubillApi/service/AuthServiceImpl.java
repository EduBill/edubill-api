package com.edubill.edubillApi.service;

import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.repository.RequestIdRepository;
import com.edubill.edubillApi.repository.UserRepository;
import com.edubill.edubillApi.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Qualifier("authServiceImpl")
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VerificationRepository redisVerificationRepository;
    private final RequestIdRepository requestIdRepository;

    @Override
    public UserRepository getUserRepository() {
        return this.userRepository;
    }
    @Override
    public VerificationRepository getVerificationRepository() {
        return this.redisVerificationRepository;
    }
    @Override
    public RequestIdRepository getRequestIdRepository() {
        return this.requestIdRepository;
    }

    @Override
    public VerificationResponseDto sendVerificationNumber(String phoneNumber) {
        // 휴대폰 번호 형식 체크
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("휴대폰 번호의 양식과 맞지 않습니다.");
        }
        // 인증번호 생성 및 저장
        final String requestId = UUID.randomUUID().toString();
        final String verificationNumber = generateRandomNumber();

        redisVerificationRepository.setVerificationNumber(requestId, verificationNumber);

        // 고유 요청 ID에 대한 인증번호를 응답
        // 실제로는 해당 전화번호를 key 값으로 sms전송
        return new VerificationResponseDto(requestId, verificationNumber);
    }



    // 인증번호 생성 (6자리)
    private static String generateRandomNumber() {
        int randomNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(randomNumber);
    }

//    private static String generateRandomNumber1() {
//        Random random = new Random();
//        int randomNumber = 100000 + random.nextInt(900000);
//        return String.valueOf(randomNumber);
//    }

    // 휴대폰 번호 형식 체크 메소드
    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^01(?:0|1|[6-9])(\\d{3,4})(\\d{4})$";
        return phoneNumber.matches(regex);
    }
}