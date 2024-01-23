package com.edubill.edubillApi.domain;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CREDIT_CARD("신용카드"),
    BANK_TRANSFER("계좌이체"),
    NAVER_PAY("네이버 페이"),
    KAKAO_PAY("카카오 페이");

    private final String paymentMethodMessage;

    PaymentMethod(String paymentMethodMessage) {
        this.paymentMethodMessage = paymentMethodMessage;
    }
}