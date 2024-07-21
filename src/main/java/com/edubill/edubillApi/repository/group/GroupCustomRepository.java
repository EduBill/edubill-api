package com.edubill.edubillApi.repository.group;

import com.edubill.edubillApi.domain.Group;

import java.util.List;

public interface GroupCustomRepository {
    List<Group> getGroupsByUserId(String userId);
}
