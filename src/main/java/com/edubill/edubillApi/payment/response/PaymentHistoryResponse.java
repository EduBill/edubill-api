package com.edubill.edubillApi.payment.response;

import java.time.LocalDateTime;

public record PaymentHistoryResponse(
        Long paymentHistoryId,
        String studentName,

        Integer paidAmount,

        LocalDateTime paidDateTime
) {
}
