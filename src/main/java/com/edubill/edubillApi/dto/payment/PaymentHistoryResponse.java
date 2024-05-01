package com.edubill.edubillApi.dto.payment;

import java.time.LocalDateTime;

public record PaymentHistoryResponse(
        Long paymentHistoryId,
        String studentName,

        Integer paidAmount,

        LocalDateTime paidDateTime
) {
}
