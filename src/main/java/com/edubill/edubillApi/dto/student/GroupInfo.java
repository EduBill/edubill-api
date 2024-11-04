package com.edubill.edubillApi.dto.student;


import com.edubill.edubillApi.domain.Group;
import lombok.Getter;

@Getter
public class GroupInfo {

    private String groupName;

    public GroupInfo() {
    }

    public GroupInfo(String groupName) {
        this.groupName = groupName;
    }

    public static GroupInfo create(Group group) {
        return new GroupInfo(group.getGroupName());
    }
}
