package com.edubill.edubillApi.dto.payment;

import lombok.*;

import java.time.YearMonth;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UnpaidHistoryRequestDto {

    private Long studentId;

    private Long paymentHistoryId;

    private YearMonth yearMonth;
}
