package com.edubill.edubillApi.error.exception;

import com.edubill.edubillApi.error.ErrorCode;
import com.edubill.edubillApi.error.exception.BusinessException;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String message) {
        super(message, ErrorCode.USER_NOT_FOUND);
    }

}
