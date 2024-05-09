package com.edubill.edubillApi.dto.payment;

import lombok.*;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentStatusDto{
        private Long paidCount;  // 납부 완료 인원

        private Long unpaidCount;  // 미 납부 인원

        private Long totalPaidAmount;

        private Long totalUnpaidAmount;

        private Boolean isExcelUploaded;
}
