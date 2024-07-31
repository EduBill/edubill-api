package com.edubill.edubillApi.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeletedGroupInfoDto {

    private Long deletedGroupId;
    private List<Long> deletedClassTimeIds;
    private List<Long> deletedStudentGroupIds;
}
