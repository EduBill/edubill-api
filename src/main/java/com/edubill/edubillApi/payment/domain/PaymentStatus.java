package com.edubill.edubillApi.payment.domain;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PAID("결제 완료"),
    UNPAID("결제 미완료");

    private final String statusMessage;

    PaymentStatus(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
