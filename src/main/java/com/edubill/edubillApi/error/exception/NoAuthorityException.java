package com.edubill.edubillApi.error.exception;

import com.edubill.edubillApi.error.ErrorCode;

public class NoAuthorityException extends BusinessException {

    public NoAuthorityException(String message) {
        super(message, ErrorCode.NO_AUTHORITY);
    }
}
