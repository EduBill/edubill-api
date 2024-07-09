package com.edubill.edubillApi.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.YearMonth;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UnpaidHistoryRequestDto {

    @Schema(description = "studentId", type = "long", example = "1")
    private Long studentId;

    @Schema(description = "paymentHistoryId", type = "long", example = "1")
    private Long paymentHistoryId;

    @Schema(description = "yearMonth", type = "string", pattern = "^\\d{4}-\\d{2}$", example = "2024-04")
    private YearMonth yearMonth;
}
