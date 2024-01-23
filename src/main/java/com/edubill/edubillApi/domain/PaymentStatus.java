package com.edubill.edubillApi.domain;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    SUCCESS("성공"),
    PENDING("보류"),
    FAILURE("실패");

    private final String statusMessage;

    PaymentStatus(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
