package com.edubill.edubillApi.error.exception;

import com.edubill.edubillApi.error.ErrorCode;
import lombok.Getter;

@Getter
public class PaymentHistoryNotFoundException extends BusinessException{
    public PaymentHistoryNotFoundException(String message) {
        super(message, ErrorCode.PAYMENT_HISTORY_NOT_FOUND);
    }
}
