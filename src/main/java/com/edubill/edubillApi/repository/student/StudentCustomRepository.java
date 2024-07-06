package com.edubill.edubillApi.repository.student;

import com.edubill.edubillApi.domain.Student;
import com.edubill.edubillApi.domain.StudentGroup;

import java.time.YearMonth;
import java.util.Collection;
import java.util.List;

public interface StudentCustomRepository {
    List<Student> findStudentsWhereDepositorNameEqualsStudentNameByYearMonthAndManagerId(String userId, YearMonth yearMonth);
    List<Student> findStudentsByDepositorNameEqualsParentName();

    List<Student> findStudentsWithDuplicateNames(Collection<StudentGroup> studentGroups);
    List<Student> findStudentsWithUniqueNames(Collection<StudentGroup> studentGroups);

    List<Student> findUnpaidStudentsByYearMonthAndManagerId(String userId, YearMonth yearMonth);

    List<Student> findPaidStudentsByYearMonthAndManagerId(String managerId, YearMonth yearMonth);
}
