package com.edubill.edubillApi.dto.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    // User
    SEND_VERIFY_NUMBER_SUCCESS("success", 200,"U001", "인증번호 전송이 완료 되었습니다."),
    VERIFY_SUCCESS("success", 200,"U002", "인증번호 확인 완료 되었습니다."),
    EXISTS_USER("success", 200,"U003", "사용자가 존재합니다."),
    NOT_EXISTS_USER("success",200, "U004", "사용자가 존재하지 않습니다"),
    SIGNUP_SUCCESS("success", 200,"U005", "회원가입 되었습니다."),
    LOGIN_SUCCESS("success", 200,"U006", "로그인 되었습니다.");



    private final String status;
    private final int status_value;
    private final String status_code;
    private final String message;
}