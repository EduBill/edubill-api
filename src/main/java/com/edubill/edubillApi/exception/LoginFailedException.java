package com.edubill.edubillApi.exception;

import lombok.Getter;

@Getter
public class LoginFailedException extends BusinessException {

    public LoginFailedException(String message) { //백에서만 보이는 메시지
        super(message, ErrorCode.LOGIN_INPUT_INVALID);
    }

}
