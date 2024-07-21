package com.edubill.edubillApi.repository.student;

import com.edubill.edubillApi.domain.Group;
import com.edubill.edubillApi.domain.QStudent;
import com.edubill.edubillApi.domain.Student;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;

import static com.edubill.edubillApi.domain.QGroup.group;
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
                .from(student, paymentHistory, group)
                .where(paymentHistory.depositDate.between(startDateTime, endDateTime)
                        .and(group.managerId.eq(managerId))
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
    public List<Student> findStudentsWithDuplicateNames(Collection<Group> groups) {
        QStudent student = QStudent.student;

        return queryFactory
                .selectFrom(student)
                .where(student.id.in(
                        JPAExpressions.select(studentGroup.student.id)
                                .from(studentGroup)
                                .where(studentGroup.group.in(groups))
                                .groupBy(studentGroup.student.id, student.studentName)
                                .having(studentGroup.student.id.count().gt(1))
                ))
                .fetch();
    }

    @Override
    public List<Student> findStudentsWithUniqueNames(Collection<Group> groups) {
        QStudent student = QStudent.student;

        return queryFactory
                .selectFrom(student)
                .where(student.id.in(
                        JPAExpressions.select(studentGroup.student.id)
                                .from(studentGroup)
                                .where(studentGroup.group.in(groups))
                                .groupBy(studentGroup.student.id, student.studentName)
                                .having(studentGroup.student.id.count().eq(1L))
                ))
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
                                .from(studentSub)
                                .join(studentSub.studentGroups, studentGroup)
                                .join(studentGroup.group, group)
                                .join(studentPaymentHistory).on(studentSub.id.eq(studentPaymentHistory.student.id))
                                .where(group.managerId.eq(managerId)
                                        .and(studentPaymentHistory.yearMonth.eq(sYearMonth))
                        )
                ))
                .fetch();
    }

    @Override
    public List<Student> findPaidStudentsByYearMonthAndManagerId(String managerId, YearMonth yearMonth) {

        String sYearMonth = yearMonth.toString();

        return queryFactory
                .selectFrom(student)
                .join(student.studentGroups, studentGroup)
                .join(studentGroup.group, group)
                .join(studentPaymentHistory).on(student.id.eq(studentPaymentHistory.student.id))
                .where(group.managerId.eq(managerId)
                        .and(studentPaymentHistory.yearMonth.eq(sYearMonth)))
                .fetch();
    }
}
