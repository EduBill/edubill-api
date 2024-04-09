package com.edubill.edubillApi.domain;

import lombok.Getter;

@Getter
public enum BankName {
    IBK("003"),
    KOOKMIN("004"),
    NONGHYUP("011"),
    WOORI("020"),
    HANA("081"),
    SHINHAN("088"),
    KAKAO("090");


    private final String bankCode;

    BankName(String bankCode) {
        this.bankCode = bankCode;
    }

    // 은행 코드에 해당하는 은행명 반환
    public static String getBankNameByCode(String code) {
        for (BankName bank : BankName.values()) {
            if (bank.getBankCode().equals(code)) {
                return bank.name();
            }
        }
        throw new IllegalArgumentException("Unsupported bank code");
    }
}
