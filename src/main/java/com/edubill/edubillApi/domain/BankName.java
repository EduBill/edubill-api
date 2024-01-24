package com.edubill.edubillApi.domain;

import lombok.Getter;

@Getter
public enum BankName {
    SINHAN("신한은행"),
    KOOKMIN("국민은행"),
    HANA("하나은행"),
    WOORI("우리은행"),
    NONGHYUP("농협은행"),
    IBK("기업은행");


    private final String bankNameDescription;

    BankName(String bankNameDescription) {
        this.bankNameDescription = bankNameDescription;
    }
}
