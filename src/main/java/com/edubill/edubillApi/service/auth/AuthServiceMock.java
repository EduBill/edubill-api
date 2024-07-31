package com.edubill.edubillApi.service.auth;

import com.edubill.edubillApi.domain.AuthInfo;
import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.domain.enums.AuthRole;
import com.edubill.edubillApi.dto.user.LoginRequestDto;
import com.edubill.edubillApi.dto.user.SignupRequestDto;
import com.edubill.edubillApi.dto.user.UserDto;
import com.edubill.edubillApi.dto.auth.VerificationResponseDto;
import com.edubill.edubillApi.error.exception.UserAlreadyExistsException;
import com.edubill.edubillApi.error.exception.UserNotFoundException;
import com.edubill.edubillApi.repository.redis.RequestIdRepository;
import com.edubill.edubillApi.repository.users.UserRepository;
import com.edubill.edubillApi.repository.verification.VerificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Qualifier("authServiceMock")
@Transactional(readOnly = true)
public class AuthServiceMock implements AuthService {

    private final UserRepository userRepository;
    private final VerificationRepository verificationRepositoryMap;
    private final RequestIdRepository requestIdRepositoryMap;

    public AuthServiceMock(UserRepository userRepository, VerificationRepository verificationRepositoryMap, RequestIdRepository requestIdRepositoryMap) {
        this.userRepository = userRepository;
        this.verificationRepositoryMap = verificationRepositoryMap;
        this.requestIdRepositoryMap = requestIdRepositoryMap;
    }

    // Mock과 구현체가 다른 메서드
    @Override
    public VerificationResponseDto sendVerificationNumber(String phoneNumber) {
        final String verificationNumber = "123456"; // 테스트 값
        final String requestId = UUID.randomUUID().toString();

        verificationRepositoryMap.setVerificationNumber(requestId, verificationNumber);

        return new VerificationResponseDto(requestId,verificationNumber);
    }

    @Override
    public Boolean verifyNumber(String requestId, String inputVerificationNumber) {
        String verificationNumber = verificationRepositoryMap.getVerificationNumber(requestId);
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
                .authInfo(new AuthInfo(requestId))
                .authRole(AuthRole.USER) // 수정 필요
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
        requestIdRepositoryMap.setRequestId(phoneNumber, requestId);
    }

    @Override
    public Boolean isRequestIdValidForPhoneNumber(String phoneNumber, String clientRequestId) {
        String storedRequestId = requestIdRepositoryMap.getRequestId(phoneNumber);
        return clientRequestId.equals(storedRequestId);
    }

}