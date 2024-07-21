package com.edubill.edubillApi.repository.student;

import com.edubill.edubillApi.domain.Student;
import com.edubill.edubillApi.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long>, StudentCustomRepository {
    List<Student> findAllByStudentGroup(Group group);
}
