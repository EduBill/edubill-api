package com.edubill.edubillApi.domain;

import lombok.Getter;

@Getter
public enum PaymentType {

    CASH("현금"),
    CREDIT_CARD("신용카드"),
    BANK_TRANSFER("계좌이체");

    private final String paymentTypeDescription;

    PaymentType(String paymentTypeDescription) {
        this.paymentTypeDescription = paymentTypeDescription;
    }
}
