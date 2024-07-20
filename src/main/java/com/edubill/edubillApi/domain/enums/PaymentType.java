package com.edubill.edubillApi.domain.enums;

import lombok.Getter;

@Getter
public enum PaymentType {

    CASH("현금결제"),
    CREDIT_CARD("카드결제"),
    KAKAO("카톡송금"),
    GIFT_VOUCHER("지역상품권"),
    BANK_TRANSFER("계좌이체"),
    ETC("기타");

    private final String paymentTypeDescription;

    PaymentType(String paymentTypeDescription) {
        this.paymentTypeDescription = paymentTypeDescription;
    }

    public static PaymentType getPaymentTypeByDescription(String description) {
        for (PaymentType type : PaymentType.values()) {
            if (type.getPaymentTypeDescription().equals(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No matching PaymentType for description: " + description);
    }
}
