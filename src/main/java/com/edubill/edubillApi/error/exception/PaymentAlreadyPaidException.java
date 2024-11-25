package com.edubill.edubillApi.error.exception;

import com.edubill.edubillApi.error.ErrorCode;

public class PaymentAlreadyPaidException extends BusinessException{
    public PaymentAlreadyPaidException(String message) {
        super(message, ErrorCode.HISTORY_ALREADY_PAID);
    }
}
