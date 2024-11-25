package com.edubill.edubillApi.error.exception;

import com.edubill.edubillApi.error.ErrorCode;
import com.edubill.edubillApi.error.ErrorResponse;

import java.util.List;

public class StudentAlreadyPaidException extends BusinessException{
    public StudentAlreadyPaidException(String message) {
        super(message, ErrorCode.STUDENT_ALREADY_PAID);
    }
}
