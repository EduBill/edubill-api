package com.edubill.edubillApi.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupIdAndNameResponseDto {

    private Long groupId;
    private String groupName;
}
