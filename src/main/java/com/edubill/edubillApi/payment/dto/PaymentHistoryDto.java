package com.edubill.edubillApi.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PaymentHistoryDto {

    private LocalDate depositDate;
    private String depositorName;
    private String bankName;
    private int depositAmount;
    private String memo;

    public PaymentHistoryDto(LocalDate depositDate, String depositorName, String bankName, int depositAmount, String memo) {
        this.depositDate = depositDate;
        this.depositorName = depositorName;
        this.bankName = bankName;
        this.depositAmount = depositAmount;
        this.memo = memo;
    }
}
