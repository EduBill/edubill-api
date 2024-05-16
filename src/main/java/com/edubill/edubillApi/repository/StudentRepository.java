package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.Student;
import com.edubill.edubillApi.domain.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long>, StudentCustomRepository {
    List<Student> findAllByStudentGroup(StudentGroup studentGroup);
}
