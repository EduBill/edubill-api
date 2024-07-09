package com.edubill.edubillApi.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnpaidStudentsResponseDto {

    @Schema(description = "studentId", type = "long", example = "1")
    private Long studentId;

    @Schema(description = "studentName", type = "string", example = "홍길동 1234")
    private String studentName;
}
