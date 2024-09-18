package com.edubill.edubillApi.dto.student;

import com.edubill.edubillApi.domain.Group;
import lombok.Getter;


@Getter
public class GroupInfoDetailResponse {

    private final String groupName;

    private GroupInfoDetailResponse(String groupName) {
        this.groupName = groupName;
    }

    public static GroupInfoDetailResponse create(Group group) {
        return new GroupInfoDetailResponse(group.getGroupName());
    }
}
