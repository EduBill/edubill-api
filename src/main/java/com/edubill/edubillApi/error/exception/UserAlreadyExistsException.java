package com.edubill.edubillApi.error.exception;

import com.edubill.edubillApi.error.ErrorCode;
import com.edubill.edubillApi.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends BusinessException {

    public UserAlreadyExistsException(String message) {
        super(message, ErrorCode.USER_DUPLICATION);
    }
}