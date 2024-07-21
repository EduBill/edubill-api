package com.edubill.edubillApi.repository.group;

import com.edubill.edubillApi.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long>, GroupCustomRepository {
}
