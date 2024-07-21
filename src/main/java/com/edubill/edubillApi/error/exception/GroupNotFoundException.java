package com.edubill.edubillApi.error.exception;

import com.edubill.edubillApi.error.ErrorCode;

public class GroupNotFoundException extends BusinessException {
    public GroupNotFoundException(String message) {
        super(message, ErrorCode.GROUP_NOT_FOUND);
    }
}
