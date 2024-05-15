package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
