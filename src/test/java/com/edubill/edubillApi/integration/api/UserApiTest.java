package com.edubill.edubillApi.integration.api;

import com.edubill.edubillApi.integration.IntegrationTest;
import com.edubill.edubillApi.integration.steps.UserSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class UserApiTest extends IntegrationTest {


    @Test
    @DisplayName("로그인 API")
    void loginApiTest() {
        // given
        String phoneNumber = "01012345678";
        String requestId = UserSteps.핸드폰인증후_회원가입_요청(phoneNumber);

        // when
        var loginRequest = UserSteps.로그인모델_생성(requestId, phoneNumber);
        var response = UserSteps.로그인_요청(loginRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("회원가입 API")
    void signUpApiTest() {
        // given
        String phoneNumber = "01012345678";
        String requestId = UserSteps.핸드폰인증_요청(phoneNumber);

        // when then
        var signUpRequest = UserSteps.회원가입모델_생성(requestId, phoneNumber);
        var response = UserSteps.회원가입_요청(signUpRequest);

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("핸드폰 인증 번호 요청 API")
    void requestVerificationNumberApiTest() {
        // given
        String phoneNumber = "01012345678";
        var verificationRequest = UserSteps.핸드폰_인증번호_모델_생성(phoneNumber);

        // when
        var response = UserSteps.핸드폰_인증번호_요청(verificationRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("핸드폰 인증 번호 확인 API")
    void verifyVerificationNumberApiTest() {
        String phoneNumber = "01012345678";
        String requestId = UserSteps.핸드폰인증_요청(phoneNumber);

        assertThat(requestId).isNotNull();
    }
}
