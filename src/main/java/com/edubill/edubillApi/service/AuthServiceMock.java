package com.edubill.edubillApi.service;

import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.repository.RequestIdRepository;
import com.edubill.edubillApi.repository.UserRepository;
import com.edubill.edubillApi.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("authServiceMock")
public class AuthServiceMock implements AuthService {

    private final UserRepository userRepository;
    private final VerificationRepository verificationRepositoryMap;
    private final RequestIdRepository requestIdRepository;

    @Override
    public UserRepository getUserRepository() {
        return this.userRepository;
    }
    @Override
    public VerificationRepository getVerificationRepository() {
        return this.verificationRepositoryMap;
    }
    @Override
    public RequestIdRepository getRequestIdRepository() {
        return this.requestIdRepository;
    }

    // Mock과 구현체가 다른 메서드
    @Override
    public VerificationResponseDto sendVerificationNumber(String phoneNumber) {
        final String verificationNumber = "123456"; // 테스트 값
        final String requestId = UUID.randomUUID().toString();

        verificationRepositoryMap.setVerificationNumber(requestId, verificationNumber);

        return new VerificationResponseDto(requestId,verificationNumber);
    }
}