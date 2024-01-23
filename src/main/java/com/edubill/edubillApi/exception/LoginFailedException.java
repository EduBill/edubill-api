package com.edubill.edubillApi.exception;

import lombok.Getter;

@Getter
public class LoginFailedException extends RuntimeException {

    private ErrorCode errorCode;

    public LoginFailedException(String message) {
        super(message); // 백에서 지정한 message
        this.errorCode = ErrorCode.LOGIN_INPUT_INVALID;
    }

    public LoginFailedException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 프런트에 보여지는 message
        this.errorCode = ErrorCode.LOGIN_INPUT_INVALID;
    }

}
