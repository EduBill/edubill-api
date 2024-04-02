package com.edubill.edubillApi.error.exception;

import com.edubill.edubillApi.error.ErrorCode;

public class AcademyNotFoundException extends BusinessException {
    public AcademyNotFoundException(String message) {
        super(message, ErrorCode.ACADEMY_NOT_FOUND);
    }
}
