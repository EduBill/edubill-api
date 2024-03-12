package com.edubill.edubillApi.error.exception;

import com.edubill.edubillApi.error.ErrorCode;
import com.edubill.edubillApi.error.ErrorResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BusinessException extends RuntimeException{

    private final ErrorCode errorCode;
    //private List<ErrorResponse.FieldError> errors = new ArrayList<>();

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

//    public BusinessException(ErrorCode errorCode, List<ErrorResponse.FieldError> errors) {
//        super(errorCode.getMessage());
//        this.errorCode = errorCode;
//        this.errors = errors;
//    }

}