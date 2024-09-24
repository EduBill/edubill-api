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
    Page<Student> findUnpaidStudentsByYearMonthAndManagerId(String userId, YearMonth yearMonth, Pageable pageable);
    List<Student> findPaidStudentsByYearMonthAndManagerId(String managerId, YearMonth yearMonth);
    List<Student> findAllByUserId(String currentUserId);

    Page<Student> getStudentsByUserIdWithPaging(String currentUserId, Pageable pageable);
    Page<Student> getStudentsByUserIdAndGroupIdOrStudentNameOrPhoneNumWithPaging(String currentUserId, Pageable pageable,Long groupId, String nameOrPhoneNum);
    Page<Student> getUnpaidStudentsByUserIdAndGroupIdOrStudentNameOrPhoneNumWithPaging(String currentUserId, Pageable pageable,Long groupId, String nameOrPhoneNum);

}
