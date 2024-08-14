package com.edubill.edubillApi.repository.group;

import com.edubill.edubillApi.domain.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupCustomRepository {
    List<Group> getGroupsByUserId(String userId);
    Page<Group> getGroupsByUserIdWithPaging(String userId, Pageable pageable);
}
