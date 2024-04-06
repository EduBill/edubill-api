package com.edubill.edubillApi.error.exception;

import com.edubill.edubillApi.error.ErrorCode;

public class StudentGroupNotFoundException extends BusinessException {
    public StudentGroupNotFoundException(String message) {
        super(message, ErrorCode.GROUP_NOT_FOUND);
    }
}
