package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.ClassTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassTimeRepository extends JpaRepository<ClassTime, Long> {
}