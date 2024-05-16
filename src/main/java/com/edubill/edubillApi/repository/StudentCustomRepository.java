package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.Student;

import java.time.YearMonth;
import java.util.List;

public interface StudentCustomRepository {
    List<Student> findStudentsWhereDepositorNameEqualsStudentNameByYearMonthAndManagerId(String userId, YearMonth yearMonth);
    List<Student> findStudentsByDepositorNameEqualsParentName();
}
