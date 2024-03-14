package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.domain.UserRole;
import com.edubill.edubillApi.dto.user.LoginRequestDto;
import com.edubill.edubillApi.dto.user.SignupRequestDto;
import com.edubill.edubillApi.dto.user.UserDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.error.exception.UserAlreadyExistsException;
import com.edubill.edubillApi.error.exception.UserNotFoundException;
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
@Service
@Transactional(readOnly = true)
@Qualifier("authServiceImpl")
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VerificationRepository redisVerificationRepository;
    private final RequestIdRepository redisRequestIdRepository;

    public AuthServiceImpl(UserRepository userRepository, VerificationRepository redisVerificationRepository, RequestIdRepository redisRequestIdRepository) {
        this.userRepository = userRepository;
        this.redisVerificationRepository = redisVerificationRepository;
        this.redisRequestIdRepository = redisRequestIdRepository;
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

    @Override
    public Boolean verifyNumber(String requestId, String inputVerificationNumber) {
        String verificationNumber = redisVerificationRepository.getVerificationNumber(requestId);
        // 6자리 코드 같을 경우 인증
        return verificationNumber.equals(inputVerificationNumber);     }

    @Override
    @Transactional
    public UserDto signUp(SignupRequestDto signupRequestDto) {
        String phoneNumber = signupRequestDto.getPhoneNumber();
        String userName = signupRequestDto.getUserName();
        String requestId = signupRequestDto.getRequestId();

        // 사용자가 이미 존재하는지 확인
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new UserAlreadyExistsException("사용자가 이미 존재함");
        }

        User user = User.builder()
                .userId(phoneNumber + "@phone.auth")
                .phoneNumber(phoneNumber)
                .userName(userName)
                .requestId(requestId)
                .userRole(UserRole.ACADEMY) // 수정 필요
                .build();

        userRepository.save(user);

        return UserDto.of(user);
    }

    @Override
    public UserDto login(LoginRequestDto loginRequestDto) {
        String phoneNumber = loginRequestDto.getPhoneNumber();
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new UserNotFoundException("사용자가 없음")
        );
        return UserDto.of(user);
    }

    @Override
    public Boolean isExistsUser(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public void requestIdForPhoneNumber(String phoneNumber, String requestId) {
        redisRequestIdRepository.setRequestId(phoneNumber, requestId);
    }

    @Override
    public Boolean isRequestIdValidForPhoneNumber(String phoneNumber, String clientRequestId) {
        String storedRequestId = redisRequestIdRepository.getRequestId(phoneNumber);
        return clientRequestId.equals(storedRequestId);
    }


    // 인증번호 생성 (6자리)
    private static String generateRandomNumber() {
        int randomNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(randomNumber);
    }

    // 휴대폰 번호 형식 체크 메소드
    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^01(?:0|1|[6-9])(\\d{3,4})(\\d{4})$";
        return phoneNumber.matches(regex);
    }
}