package com.edubill.edubillApi.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends BusinessException {

    public UserAlreadyExistsException(String message) {
        super(message, ErrorCode.USER_DUPLICATION);
    }
}