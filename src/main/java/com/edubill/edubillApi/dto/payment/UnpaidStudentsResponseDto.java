package com.edubill.edubillApi.dto.payment;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnpaidStudentsResponseDto {

    private Long studentId;

    private String studentName;
}
