package com.edubill.edubillApi.dto.payment;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UnpaidHistoryRequestDto {

    private Long studentId;

    private Long paymentHistoryId;

    private String yearMonth;
}
