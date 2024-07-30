package com.edubill.edubillApi.dto.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeletedStudentInfoDto {

    private Long deletedStudentId;
    private List<Long> deletedStudentGroupIds;
    private List<Long> deletedPaymentKeyIds;
    private List<Long> deletedStudentPaymentHistoryIds;
}
