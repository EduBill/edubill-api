package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.dto.user.SignUpRequestDto;
import com.edubill.edubillApi.dto.user.UserDto;
import com.edubill.edubillApi.dto.verification.ExistUserRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.exception.UserAlreadyExistsException;
import com.edubill.edubillApi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Slf4j
class AuthServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("인증 확인")
    void verificationTest() {
        //given
        String phoneNumber = "01012345678";
        VerificationResponseDto verificationResponse = authService.sendVerificationNumber(phoneNumber);
        String verificationNumber = verificationResponse.getVerificationNumber();

        //when
        VerificationRequestDto verificationRequest = new VerificationRequestDto(verificationNumber, verificationResponse.getRequestId());

        //then
        String message = authService.verifyNumber(verificationRequest.getVerificationNumber(), verificationRequest.getRequestId());
        assertThat(message).isEqualTo("인증 완료 되었습니다.");
    }

    @Test
    @DisplayName("회원 가입")
    void signUpTest() {
        //given
        String requestId = UUID.randomUUID().toString();
        SignUpRequestDto requestA = new SignUpRequestDto("userA", "01012345678", requestId);

        //when
        UserDto userA = authService.signUp(requestA);

        //then
        User findUser = userRepository.findByPhoneNumber("01012345678").orElse(null);
        log.info("findUser={}", findUser);
        assertThat(findUser.getUserId()).isEqualTo(userA.getUserId());
    }

    @Test
    @DisplayName("중복 회원가입 실패")
    void signUpDuplicateTest() {
        //given
        String requestId = UUID.randomUUID().toString();
        SignUpRequestDto signUpRequest = new SignUpRequestDto("userA", "01011111111", requestId);
        UserDto userA = authService.signUp(signUpRequest);

        //when
        ExistUserRequestDto existRequest = new ExistUserRequestDto("01011111111", requestId);

        //then
//        assertThatThrownBy(() -> authService.isExistsUser(existRequest.getPhoneNumber()))
//                .isInstanceOf(UserAlreadyExistsException.class);
        assertThat(authService.isExistsUser(existRequest.getPhoneNumber())).isTrue();
    }

}