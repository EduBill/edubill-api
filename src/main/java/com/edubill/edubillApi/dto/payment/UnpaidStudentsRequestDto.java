package com.edubill.edubillApi.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnpaidStudentsRequestDto {

    private String userId;

    private YearMonth yearMonth;
}
