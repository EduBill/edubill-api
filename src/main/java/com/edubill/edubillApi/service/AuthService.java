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
import org.springframework.transaction.annotation.Transactional;


public interface AuthService {
    UserRepository getUserRepository();
    VerificationRepository getVerificationRepository();
    RequestIdRepository getRequestIdRepository();


    VerificationResponseDto sendVerificationNumber(String phoneNumber);

    default Boolean verifyNumber(String requestId, String InputVerificationNumber) {
        VerificationRepository verificationRepository = getVerificationRepository();

        String verificationNumber = verificationRepository.getVerificationNumber(requestId);
        // 6자리 코드 같을 경우 인증
        return verificationNumber.equals(InputVerificationNumber);
    }

    @Transactional
    default UserDto signUp(SignupRequestDto signupRequestDto) {
        UserRepository userRepository = getUserRepository();

        String phoneNumber = signupRequestDto.getPhoneNumber();
        String userName = signupRequestDto.getUserName();
        String requestId = signupRequestDto.getRequestId();

        // 사용자가 이미 존재하는지 확인
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new UserAlreadyExistsException("사용자가 이미 존재함");
        }

        // userId 설정 : 01012345678@phone.auth
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

    @Transactional(readOnly = true)
    default UserDto login(LoginRequestDto loginRequestDto) {
        UserRepository userRepository = getUserRepository();

        String phoneNumber = loginRequestDto.getPhoneNumber();
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new UserNotFoundException("사용자가 없음")
        );
        return UserDto.of(user);// user객체를 dto에 담아서 반환
    }

    // 사용자 가입 여부 조회
    default Boolean isExistsUser(String phoneNumber) {
        UserRepository userRepository = getUserRepository();
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    default void requestIdForPhoneNumber(String phoneNumber, String requestId){
        RequestIdRepository requestIdRepository = getRequestIdRepository();
        // 실제로는 redis에 {key : value} = {phoneNumber : requestId} 의 형태로 저장
        requestIdRepository.setRequestId(phoneNumber, requestId);
    }

    default Boolean isRequestIdValidForPhoneNumber(String phoneNumber, String clientRequestId){
        RequestIdRepository requestIdRepository = getRequestIdRepository();
        String storedRequestId = requestIdRepository.getRequestId(phoneNumber);
        return clientRequestId.equals(storedRequestId);
    };

}