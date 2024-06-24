package com.edubill.edubillApi.error.exception;

import com.edubill.edubillApi.error.ErrorCode;

public class PaymentKeyNotEncryptedException extends BusinessException {
    public PaymentKeyNotEncryptedException(String message) {
        super(message, ErrorCode.PAYMENT_KEY_NOT_ENCRYPTED);
    }
}
