package com.edubill.edubillApi.exception;

public class RefreshTokenInvalidException extends RuntimeException{

    public RefreshTokenInvalidException() {
        super("유효하지 않은 리프레시 토큰입니다.");
    }

    public RefreshTokenInvalidException(String message) {
        super(message);
    }

}
