package com.edubill.edubillApi.payment.response;

import java.time.LocalDateTime;

public record PaymentHistoryResponse(
        String studentName,

        Integer paidAmount,

        LocalDateTime paidDateTime
) {
}
