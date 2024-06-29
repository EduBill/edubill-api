package com.edubill.edubillApi.dto.payment;

import java.time.LocalDateTime;

public record PaymentHistoryResponseDto(
        Long paymentHistoryId,
        String studentName,

        Integer paidAmount,

        LocalDateTime paidDateTime
) {
}
