package com.edubill.edubillApi.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoRequestDto {

    @Schema(description = "paymentHistoryId", type = "long", example = "1")
    private long paymentHistoryId;
    @Schema(description = "memoDescription", type = "String", example = "메모메모메모")
    private String memoDescription;
}