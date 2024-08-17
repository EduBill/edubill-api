package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.ClassTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassTimeRepository extends JpaRepository<ClassTime, Long> {
    List<ClassTime> findByGroupId(long groupId);
}
