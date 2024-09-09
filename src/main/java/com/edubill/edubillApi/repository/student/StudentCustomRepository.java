package com.edubill.edubillApi.repository.student;

import com.edubill.edubillApi.domain.Student;
import com.edubill.edubillApi.domain.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;
import java.util.Collection;
import java.util.List;

public interface StudentCustomRepository {

    List<Student> findStudentsWithDuplicateNames(Collection<Group> groups);
    List<Student> findStudentsWithUniqueNames(Collection<Group> groups);
    List<Student> findUnpaidStudentsByYearMonthAndManagerId(String userId, YearMonth yearMonth);
    List<Student> findPaidStudentsByYearMonthAndManagerId(String managerId, YearMonth yearMonth);
    List<Student> findAllByUserId(String currentUserId);

    Page<Student> getStudentsByUserIdWithPaging(String currentUserId, Pageable pageable);
}
