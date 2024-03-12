package com.edubill.edubillApi.dto.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SEND_VERIFY_NUMBER_SUCCESS("success", 200, "인증번호 전송이 완료 되었습니다."),
    VERIFY_SUCCESS("success", 200, "인증번호 확인 완료 되었습니다."),
    EXISTS_USER("success", 200, "사용자가 존재합니다."),
    NOT_EXISTS_USER("success", 200, "사용자가 존재하지 않습니다"),
    SIGNUP_SUCCESS("success", 200, "회원가입 되었습니다."),
    LOGIN_SUCCESS("success", 200, "로그인 되었습니다.");



    private final String status;
    private final int status_code;
    private final String message;
}