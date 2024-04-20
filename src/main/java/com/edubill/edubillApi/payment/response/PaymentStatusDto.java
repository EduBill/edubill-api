package com.edubill.edubillApi.payment.response;

public record PaymentStatusDto(
        Long paidCount,   // 납부 완료 인원
        Long unpaidCount,   // 미 납부 인원

        Long totalPaidAmount,

        Long totalunPaidAmount
) {
}