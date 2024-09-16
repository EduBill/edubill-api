package com.edubill.edubillApi.integration.steps;

import com.edubill.edubillApi.dto.auth.VerificationNumberRequestDto;
import com.edubill.edubillApi.dto.auth.VerificationRequestDto;
import com.edubill.edubillApi.dto.user.LoginRequestDto;
import com.edubill.edubillApi.dto.user.SignupRequestDto;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class UserSteps {

    public static LoginRequestDto 로그인모델_생성(String requestId, String phoneNumber) {
        return new LoginRequestDto(requestId, phoneNumber);
    }

    public static ExtractableResponse<Response> 로그인_요청(LoginRequestDto loginRequestDto) {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(loginRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/v1/auth/login")
                .then().log().all()
                .extract();

        return response;
    }

    public static VerificationNumberRequestDto 핸드폰_인증번호_모델_생성(String phoneNumber) {
        return new VerificationNumberRequestDto(phoneNumber);
    }

    public static ExtractableResponse<Response> 핸드폰_인증번호_요청(VerificationNumberRequestDto verificationNumberRequestDto) {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(verificationNumberRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/v1/auth/phone")
                .then().log().all()
                .extract();

        return response;
    }

    public static VerificationRequestDto 핸드폰인증확인_모델_생성(String requestId, String verificationNumber,String phoneNumber) {
        return new VerificationRequestDto(requestId, verificationNumber ,phoneNumber);
    }

    public static ExtractableResponse<Response> 핸드폰인증확인_요청(VerificationRequestDto verificationRequestDto) {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(verificationRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/v1/auth/verify")
                .then().log().all()
                .extract();

        return response;
    }


    public static SignupRequestDto 회원가입모델_생성(final String requestId, final String phoneNumber) {
        return new SignupRequestDto(requestId, "userA", phoneNumber);
    }

    public static ExtractableResponse<Response> 회원가입_요청(SignupRequestDto signupRequestDto) {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(signupRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/v1/auth/signup")
                .then().log().all()
                .extract();

        return response;
    }

    public static String 핸드폰인증후_회원가입_요청(String phoneNumber) {
        String requestId = 핸드폰인증_요청(phoneNumber);

        var signUpRequest = UserSteps.회원가입모델_생성(requestId, phoneNumber);
        var signUpResponse = UserSteps.회원가입_요청(signUpRequest);

        assertThat(signUpResponse.statusCode()).isEqualTo(200);

        return requestId;
    }

    public static String 핸드폰인증_요청(String phoneNumber) {
        var verificationRequest = UserSteps.핸드폰_인증번호_모델_생성(phoneNumber);
        var verificationResponse = UserSteps.핸드폰_인증번호_요청(verificationRequest);
        var verificationCode = verificationResponse.body().jsonPath().getString("verificationNumber");
        var requestId = verificationResponse.body().jsonPath().getString("requestId");

        var verificationModel = UserSteps.핸드폰인증확인_모델_생성(requestId, verificationCode, phoneNumber);
        var verifyModel = UserSteps.핸드폰인증확인_요청(verificationModel);

        return requestId; // requestId 반환
    }
}
