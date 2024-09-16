package com.edubill.edubillApi.dto.student;

import com.edubill.edubillApi.domain.Group;

public class GroupInfoDetailResponse {

    private final String groupName;

    private GroupInfoDetailResponse(String groupName) {
        this.groupName = groupName;
    }

    public static GroupInfoDetailResponse create(Group group) {
        return new GroupInfoDetailResponse(group.getGroupName());
    }
    public String getGroupName() {
        return groupName;
    }
}
