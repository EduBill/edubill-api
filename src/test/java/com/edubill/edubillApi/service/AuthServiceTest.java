package com.edubill.edubillApi.service;

import com.edubill.edubillApi.dto.verification.VerificationRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    // 인증확인
    @Test
    void verificationTest() {
        //given
        String phoneNumber = "01012345678";
        VerificationResponseDto verificationResponse = authService.sendVerificationNumber(phoneNumber);
        String verificationNumber = verificationResponse.getVerificationNumber();

        //when
        VerificationRequestDto verificationRequest = new VerificationRequestDto(verificationNumber, verificationResponse.getRequestId());

        //then
        String message = authService.verifyNumber(verificationRequest.getEnteredNumber(), verificationRequest.getRequestId());
        assertThat(message).isEqualTo("인증 완료 되었습니다.");
    }

    // 회원가입
    @Test
    void joinTest() {
        //given

        //when

        //then

    }

}