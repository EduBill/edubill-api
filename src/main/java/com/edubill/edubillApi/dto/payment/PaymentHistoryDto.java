package com.edubill.edubillApi.dto.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PaymentHistoryDto {

    private LocalDateTime depositDate;
    private String depositorName;
    private String bankName;
    private int depositAmount;
    private String memo;

    public PaymentHistoryDto(LocalDateTime depositDate, String depositorName, String bankName, int depositAmount, String memo) {
        this.depositDate = depositDate;
        this.depositorName = depositorName;
        this.bankName = bankName;
        this.depositAmount = depositAmount;
        this.memo = memo;
    }
}
