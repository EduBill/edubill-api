package com.edubill.edubillApi.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "userId", type = "long", example = "1")
    private String userId;

    @Schema(description = "yearMonth", type = "string", pattern = "^\\d{4}-\\d{2}$", example = "2024-04")
    private YearMonth yearMonth;
}
