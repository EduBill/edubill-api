package com.edubill.edubillApi.repository.student;

import com.edubill.edubillApi.domain.QStudent;
import com.edubill.edubillApi.domain.Student;
import com.edubill.edubillApi.domain.StudentGroup;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;

import static com.edubill.edubillApi.domain.QPaymentHistory.paymentHistory;
import static com.edubill.edubillApi.domain.QStudent.student;
import static com.edubill.edubillApi.domain.QStudentGroup.studentGroup;
import static com.edubill.edubillApi.domain.QStudentPaymentHistory.studentPaymentHistory;

@Repository
@RequiredArgsConstructor
public class StudentCustomRepositoryImpl implements StudentCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Student> findStudentsWhereDepositorNameEqualsStudentNameByYearMonthAndManagerId(String managerId, YearMonth yearMonth) {

        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999);

        return queryFactory
                .select(student)
                .from(student, paymentHistory, studentGroup)
                .where(paymentHistory.depositDate.between(startDateTime, endDateTime)
                        .and(studentGroup.managerId.eq(managerId))
                        .and(student.studentName.eq(paymentHistory.depositorName)))
                .fetch();
    }

    @Override
    public List<Student> findStudentsByDepositorNameEqualsParentName() {
        return queryFactory
                .select(student)
                .from(student, paymentHistory)
                .where(student.parentName.eq(paymentHistory.depositorName))
                .fetch();
    }

    @Override
    public List<Student> findStudentsWithDuplicateNames(Collection<StudentGroup> studentGroups) {
        QStudent student = QStudent.student;

        return queryFactory
                .selectFrom(student)
                .where(student.studentGroup.in(studentGroups)
                        .and(student.studentName.in(
                                JPAExpressions.select(student.studentName)
                                        .from(student)
                                        .groupBy(student.studentName)
                                        .having(student.count().gt(1))
                        )))
                .fetch();
    }

    @Override
    public List<Student> findStudentsWithUniqueNames(Collection<StudentGroup> studentGroups) {
        QStudent student = QStudent.student;

        return queryFactory
                .selectFrom(student)
                .where(student.studentGroup.in(studentGroups)
                        .and(student.studentName.in(
                                JPAExpressions.select(student.studentName)
                                        .from(student)
                                        .groupBy(student.studentName)
                                        .having(student.count().eq(1L))
                        )))
                .fetch();
    }

    public List<Student> findUnpaidStudentsByYearMonthAndManagerId(String managerId, YearMonth yearMonth) {

        String sYearMonth = yearMonth.toString();
        QStudent studentSub = new QStudent("studentSub");

        return queryFactory
                .selectFrom(student)
                .where(student.id.notIn(
                        JPAExpressions
                                .select(studentSub.id)
                                .from(studentSub, studentPaymentHistory, studentGroup)
                                .where(studentGroup.managerId.eq(managerId)
                                        .and(studentPaymentHistory.yearMonth.eq(sYearMonth))
                                        .and(studentSub.id.eq(studentPaymentHistory.student.id)))
                        ))
                .fetch();
    }
}
