package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.dto.user.SignupRequestDto;
import com.edubill.edubillApi.dto.user.UserDto;
import com.edubill.edubillApi.dto.user.ExistUserRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Slf4j
class AuthServiceImplTest {

    @Autowired
    private AuthServiceMock authServiceMock;
    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("인증 확인")
    void verificationTest() {
        //given
        String phoneNumber = "01012345678";
        VerificationResponseDto verificationResponse = authServiceMock.sendVerificationNumber(phoneNumber);

        //when
        VerificationRequestDto verificationRequest = new VerificationRequestDto("123456", verificationResponse.getRequestId());

        //then
        Boolean isVerify = authServiceMock.verifyNumber(verificationRequest.getVerificationNumber(), verificationRequest.getRequestId());
        assertThat(isVerify).isEqualTo(true);
    }

    @Test
    @DisplayName("회원 가입")
    void signUpTest() {
        //given
        String requestId = UUID.randomUUID().toString();
        SignupRequestDto requestA = new SignupRequestDto("userA", "01012345678", requestId);

        //when
        UserDto userA = authServiceMock.signUp(requestA);

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
        SignupRequestDto signUpRequest = new SignupRequestDto("userA", "01011111111", requestId);
        UserDto userA = authServiceMock.signUp(signUpRequest);

        //when
        ExistUserRequestDto existRequest = new ExistUserRequestDto("01011111111", requestId);

        //then
//        assertThatThrownBy(() -> authService.isExistsUser(existRequest.getPhoneNumber()))
//                .isInstanceOf(UserAlreadyExistsException.class);
        assertThat(authServiceMock.isExistsUser(existRequest.getPhoneNumber())).isTrue();
    }

}