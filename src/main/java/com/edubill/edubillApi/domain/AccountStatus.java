package com.edubill.edubillApi.domain;

import lombok.Getter;

@Getter
public enum AccountStatus {
    ACTIVE("활성화"),
    INACTIVE("비활성화"),
    PENDING("보류");
    //CLOSED("폐쇄");

    private final String statusMessage;

    AccountStatus(String statusMessage) {
        this.statusMessage = statusMessage;
    }

}
