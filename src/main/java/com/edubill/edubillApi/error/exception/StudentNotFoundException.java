package com.edubill.edubillApi.error.exception;

import com.edubill.edubillApi.error.ErrorCode;

public class StudentNotFoundException extends BusinessException {
    public StudentNotFoundException(String message) {
        super(message, ErrorCode.STUDENT_NOT_FOUND);
    }
}