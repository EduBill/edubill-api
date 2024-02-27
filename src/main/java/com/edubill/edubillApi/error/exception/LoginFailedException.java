package com.edubill.edubillApi.error.exception;

import com.edubill.edubillApi.error.ErrorCode;
import lombok.Getter;

@Getter
public class LoginFailedException extends BusinessException {

    public LoginFailedException(String message) { //백에서만 보이는 메시지
        super(message, ErrorCode.LOGIN_INPUT_INVALID);
    }

}