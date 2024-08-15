package com.edubill.edubillApi.error.exception;

import com.edubill.edubillApi.error.ErrorCode;
import com.edubill.edubillApi.error.ErrorResponse;

import java.util.List;

public class ParseNotFoundException extends BusinessException{
    public ParseNotFoundException(String message) {
        super(message, ErrorCode.INVALID_DATETIME_VALUE);
    }
}
