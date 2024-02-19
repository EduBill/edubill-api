package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.domain.UserRole;
import com.edubill.edubillApi.dto.user.LoginRequestDto;
import com.edubill.edubillApi.dto.user.SignupRequestDto;
import com.edubill.edubillApi.dto.user.UserDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.exception.UserNotFoundException;
import com.edubill.edubillApi.repository.UserRepository;
import com.edubill.edubillApi.repository.VerificationRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


public interface AuthService {
    VerificationRepository getVerificationRepository();
    UserRepository getUserRepository();


    VerificationResponseDto sendVerificationNumber(String phoneNumber);

    default Boolean verifyNumber(String InputNumber, String requestId) {
        VerificationRepository verificationRepository = getVerificationRepository();

        String verificationNumber = verificationRepository.getVerificationNumber(requestId);
        // requestId에 대한 검증 번호가 존재하는지 확인
        if (verificationNumber == null) {
            throw new NoSuchElementException("해당요청에 대한 인증번호가 존재하지 않습니다. : " + requestId);
        }
        // 6자리 코드 같을 경우 인증
        boolean isTrue = verificationNumber.equals(InputNumber);
        if (!isTrue) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다. (InputNumber: " + InputNumber + ")");
        }
        return isTrue;
    }

    @Transactional
    default UserDto signUp(SignupRequestDto signupRequestDto) {
        UserRepository userRepository = getUserRepository();

        String phoneNumber = signupRequestDto.getPhoneNumber();
        String userName = signupRequestDto.getUserName();
        String requestId = signupRequestDto.getRequestId();

        User user = User.builder()
                .phoneNumber(phoneNumber)
                .userName(userName)
                .requestId(requestId)
                .userRole(UserRole.ADMIN) // 수정필요
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
}