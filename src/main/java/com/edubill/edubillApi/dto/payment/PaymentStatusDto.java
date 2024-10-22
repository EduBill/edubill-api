package com.edubill.edubillApi.dto.payment;

import lombok.*;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentStatusDto{
        private Long paidCount;  // 납부 완료 인원

        private Long unpaidCount;  // 미 납부 인원

        private Long unCheckedCount; // 미확인 납부 인원

        private Long totalPaidAmount; // 납부 완료 금액

        private Long totalUnpaidAmount; // 미납부 금액

        private Long totalUnCheckedAmount; // 미확인 납부 금액 : 학생 목록에는 없으나 입금내역에는 존재

        private Boolean isExcelUploaded; //default는 false
}
