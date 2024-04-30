package com.edubill.edubillApi.payment.dto;

import com.edubill.edubillApi.payment.domain.PaymentHistory;
import com.edubill.edubillApi.payment.domain.PaymentType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentHistoryDetailResponse {

    private String depositorName; //이름
    private int paidAmount; // 입금액
    private String paymentTypeDescription; //거래 유형
    private String memo;  // 메모
    private LocalDateTime depositDate; //거래일시

    public static PaymentHistoryDetailResponse of(PaymentHistory paymentHistory) {
        return new PaymentHistoryDetailResponse(
                paymentHistory.getDepositorName(),
                paymentHistory.getPaidAmount(),
                paymentHistory.getPaymentType().getPaymentTypeDescription(),
                paymentHistory.getMemo(),
                paymentHistory.getDepositDate()
        );
    }

}
