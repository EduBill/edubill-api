package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {
}
