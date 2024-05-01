package com.edubill.edubillApi.dto.payment;

public record PaymentStatusDto(
        Long paidCount,   // 납부 완료 인원
        Long unpaidCount,   // 미 납부 인원

        Long totalPaidAmount,

        Long totalUnpaidAmount
) {
}
