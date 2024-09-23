package com.edubill.edubillApi.repository.student;

import com.edubill.edubillApi.domain.*;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.edubill.edubillApi.domain.QGroup.group;
import static com.edubill.edubillApi.domain.QPaymentHistory.paymentHistory;
import static com.edubill.edubillApi.domain.QStudent.student;

import static com.edubill.edubillApi.domain.QStudentGroup.studentGroup;
import static com.edubill.edubillApi.domain.QStudentPaymentHistory.studentPaymentHistory;
import static com.querydsl.core.types.Order.ASC;

@Repository
@RequiredArgsConstructor
public class StudentCustomRepositoryImpl implements StudentCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Student> findStudentsWithDuplicateNames(Collection<Group> groups) {
        QStudent student = QStudent.student;

        // 그룹의 ID 리스트를 추출
        List<Long> groupIds = groups.stream()
                .map(Group::getId)
                .collect(Collectors.toList());

        return queryFactory
                .selectFrom(student)
                .where(student.studentGroups.any().group.id.in(groupIds)
                        .and(student.studentName.in(
                                JPAExpressions.select(student.studentName)
                                        .from(student)
                                        .groupBy(student.studentName)
                                        .having(student.count().gt(1)))))
                .fetch();
    }

    @Override
    public List<Student> findStudentsWithUniqueNames(Collection<Group> groups) {

        QStudent student = QStudent.student;

        // 그룹의 ID 리스트를 추출
        List<Long> groupIds = groups.stream()
                .map(Group::getId)
                .collect(Collectors.toList());

        return queryFactory
                .selectFrom(student)
                .where(student.studentGroups.any().group.id.in(groupIds)
                        .and(student.studentName.in(
                                JPAExpressions.select(student.studentName)
                                        .from(student)
                                        .groupBy(student.studentName)
                                        .having(student.count().eq(1L)))))
                .fetch();
    }

    public Page<Student> findUnpaidStudentsByYearMonthAndManagerId(String managerId, YearMonth yearMonth, Pageable pageable) {

        String sYearMonth = yearMonth.toString();

        JPQLQuery<Student> query = queryFactory
                .selectFrom(student)
                .where(student.id.notIn(
                        JPAExpressions
                                .select(student.id)
                                .from(studentPaymentHistory)
                                .join(studentPaymentHistory.student, student)
                                .join(studentPaymentHistory.student.studentGroups, studentGroup)
                                .join(studentGroup.group, group)
                                .where(group.managerId.eq(managerId)
                                        .and(studentPaymentHistory.yearMonth.eq(sYearMonth))
                                )

                ))
                .orderBy(new OrderSpecifier(ASC, student.studentName));

        // 페이징 및 정렬 설정
        QueryResults<Student> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());

    }

    @Override
    public List<Student> findPaidStudentsByYearMonthAndManagerId(String managerId, YearMonth yearMonth) {

        String sYearMonth = yearMonth.toString();

        return queryFactory
                .select(student)
                .from(studentPaymentHistory)
                .join(studentPaymentHistory.student, student)
                .join(studentPaymentHistory.student.studentGroups, studentGroup)
                .join(studentGroup.group, group)
                .where(group.managerId.eq(managerId)
                        .and(studentPaymentHistory.yearMonth.eq(sYearMonth)))
                .fetch();
    }

    @Override
    public List<Student> findAllByUserId(String currentUserId) {
        QGroup group = QGroup.group;
        QStudentGroup studentGroup = QStudentGroup.studentGroup;
        QStudent student = QStudent.student;

        // 기본 쿼리 설정
        return queryFactory
                .select(student)
                .from(studentGroup)
                .join(studentGroup.student, student)
                .join(studentGroup.group, group)
                .where(group.managerId.eq(currentUserId))
                .fetch();
    }

    @Override
    public Page<Student> getStudentsByUserIdWithPaging(String managerId, Pageable pageable) {
        QGroup group = QGroup.group;
        QStudentGroup studentGroup = QStudentGroup.studentGroup;
        QStudent student = QStudent.student;

       // 기본 쿼리 설정
        JPQLQuery<Student> query = queryFactory
                .select(student)
                .from(studentGroup)
                .join(studentGroup.student, student)
                .join(studentGroup.group, group)
                .where(group.managerId.eq(managerId))
                .orderBy(new OrderSpecifier(ASC, student.studentName));

        // 페이징 및 정렬 설정
        QueryResults<Student> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public Page<Student> getStudentsByUserIdAndGroupIdAndNameWithPaging(String currentUserId, Pageable pageable, Long groupId, String name) {
        QGroup group = QGroup.group;
        QStudentGroup studentGroup = QStudentGroup.studentGroup;
        QStudent student = QStudent.student;

        // 기본 쿼리 설정
        JPQLQuery<Student> query = queryFactory
                .select(student)
                .from(studentGroup)
                .join(studentGroup.student, student)
                .join(studentGroup.group, group)
                .where(group.managerId.eq(currentUserId)
                        .and(student.studentName.eq(name))
                        .and(group.id.eq(groupId)))
                .orderBy(new OrderSpecifier(ASC, student.studentName));

        // 페이징 및 정렬 설정
        QueryResults<Student> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public Page<Student> getUnpaidStudentsByUserIdAndGroupIdAndNameWithPaging(String currentUserId, Pageable pageable, Long groupId, String name) {
        String sYearMonth = YearMonth.now().toString();

        JPQLQuery<Student> query = queryFactory
                .selectFrom(student)
                .where(student.id.notIn(
                        JPAExpressions
                                .select(student.id)
                                .from(studentPaymentHistory)
                                .join(studentPaymentHistory.student, student)
                                .join(studentPaymentHistory.student.studentGroups, studentGroup)
                                .join(studentGroup.group, group)
                                .where(group.managerId.eq(currentUserId)
                                        .and(studentPaymentHistory.yearMonth.eq(sYearMonth)
                                        .and(student.studentName.eq(name))
                                        .and(group.id.eq(groupId)))
                                )

                ))
                .orderBy(new OrderSpecifier(ASC, student.studentName));

        // 페이징 및 정렬 설정
        QueryResults<Student> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public Page<Student> getStudentsByUserIdAndGroupIdAndPhoneNumberWithPaging(String currentUserId, Pageable pageable, Long groupId, String phoneNumber) {
        QGroup group = QGroup.group;
        QStudentGroup studentGroup = QStudentGroup.studentGroup;
        QStudent student = QStudent.student;

        // 기본 쿼리 설정
        JPQLQuery<Student> query = queryFactory
                .select(student)
                .from(studentGroup)
                .join(studentGroup.student, student)
                .join(studentGroup.group, group)
                .where(group.managerId.eq(currentUserId)
                        .and(student.studentPhoneNumber.eq(phoneNumber))
                        .and(group.id.eq(groupId)))
                .orderBy(new OrderSpecifier(ASC, student.studentName));

        // 페이징 및 정렬 설정
        QueryResults<Student> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public Page<Student> getUnpaidStudentsByUserIdAndGroupIdAndPhoneNumberWithPaging(String currentUserId, Pageable pageable, Long groupId, String phoneNumber) {
        String sYearMonth = YearMonth.now().toString();

        JPQLQuery<Student> query = queryFactory
                .selectFrom(student)
                .where(student.id.notIn(
                        JPAExpressions
                                .select(student.id)
                                .from(studentPaymentHistory)
                                .join(studentPaymentHistory.student, student)
                                .join(studentPaymentHistory.student.studentGroups, studentGroup)
                                .join(studentGroup.group, group)
                                .where(group.managerId.eq(currentUserId)
                                        .and(studentPaymentHistory.yearMonth.eq(sYearMonth)
                                                .and(student.studentPhoneNumber.eq(phoneNumber))
                                                .and(group.id.eq(groupId)))
                                )

                ))
                .orderBy(new OrderSpecifier(ASC, student.studentName));

        // 페이징 및 정렬 설정
        QueryResults<Student> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }


}
