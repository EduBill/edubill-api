package com.edubill.edubillApi.repository.payment;

import com.edubill.edubillApi.domain.PaymentHistory;
import com.edubill.edubillApi.domain.enums.PaymentStatus;
import com.querydsl.core.types.dsl.BooleanExpression;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static com.edubill.edubillApi.domain.QGroup.group;
import static com.edubill.edubillApi.domain.QPaymentHistory.paymentHistory;

import static com.edubill.edubillApi.domain.QStudent.student;
import static com.edubill.edubillApi.domain.QStudentGroup.studentGroup;
import static com.edubill.edubillApi.domain.QStudentPaymentHistory.studentPaymentHistory;
import static com.edubill.edubillApi.domain.QUser.user;


@Repository
public class PaymentHistoryCustomRepositoryImpl implements PaymentHistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    public PaymentHistoryCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    @Transactional
    public Page<PaymentHistory> findPaymentHistoriesByYearMonthAndManagerId(String userId, YearMonth yearMonth, Pageable pageable) {
        // 월의 첫째 날
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay(); // 해당 월의 첫 날 자정
        // 월의 마지막 날
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999); // 해당 월의 마지막 날 23시 59분 59초 999999999나노초
        // 쿼리를 위한 기본 조건
        BooleanExpression predicate = paymentHistory.depositDate.between(startDateTime, endDateTime)
                .and(paymentHistory.paymentStatus.eq(PaymentStatus.PAID))
                .and(user.userId.eq(userId));

        // 전체 카운트를 위한 쿼리
        long total = queryFactory
                .selectFrom(paymentHistory)
                .join(user)
                .on(paymentHistory.managerId.eq(user.userId))
                .where(predicate)
                .fetch().size();

        // 페이지 처리를 위한 결과 리스트
        List<PaymentHistory> results = queryFactory
                .selectFrom(paymentHistory)
                .join(user)
                .on(paymentHistory.managerId.eq(user.userId))
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 결과를 Page 객체로 반환
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<PaymentHistory> findUnpaidHistoriesByYearMonthAndManagerId(String userId, YearMonth yearMonth, Pageable pageable) {

        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999);

        BooleanExpression predicate = paymentHistory.depositDate.between(startDateTime, endDateTime)
                .and(paymentHistory.paymentStatus.eq(PaymentStatus.UNPAID))
                .and(user.userId.eq(userId));

        int total = queryFactory
                .selectFrom(paymentHistory)
                .join(user)
                .on(paymentHistory.managerId.eq(user.userId))
                .where(predicate)
                .fetch().size();

        List<PaymentHistory> pagedResults = queryFactory
                .selectFrom(paymentHistory)
                .join(user)
                .on(paymentHistory.managerId.eq(user.userId))
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(pagedResults, pageable, total);
    }


    @Override
    public List<PaymentHistory> findPaymentHistoriesByUserIdAndYearMonthWithPaymentStatusPaid(String userId, YearMonth yearMonth) {
        // 월의 첫째 날
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay(); // 해당 월의 첫 날 자정
        // 월의 마지막 날
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999); // 해당 월의 마지막 날 23시 59분 59초 999999999나노초

        return queryFactory
                .selectFrom(paymentHistory)
                .join(user)
                .on(paymentHistory.managerId.eq(user.userId))
                .where(paymentHistory.depositDate.between(startDateTime, endDateTime)
                        .and(user.userId.eq(userId))
                        .and(paymentHistory.paymentStatus.eq(PaymentStatus.PAID)))
                .fetch();
    }

    @Override
    public List<PaymentHistory> findPaymentHistoriesByManagerIdAndYearMonthWithPaymentStatusUnPaid(String managerId, YearMonth yearMonth) {
        // 월의 첫째 날
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay(); // 해당 월의 첫 날 자정
        // 월의 마지막 날
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999); // 해당 월의 마지막 날 23시 59분 59초 999999999나노초

        return queryFactory
                .selectFrom(paymentHistory)
                .join(user)
                .on(paymentHistory.managerId.eq(user.userId))
                .where(paymentHistory.depositDate.between(startDateTime, endDateTime)
                        .and(user.userId.eq(managerId))
                        .and(paymentHistory.paymentStatus.eq(PaymentStatus.UNPAID)))
                .fetch();
    }

    @Override
    public List<PaymentHistory> findPaymentHistoriesByManagerIdAndYearMonth(String userId, YearMonth yearMonth){

        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999);

        return queryFactory
                .selectFrom(paymentHistory)
                .join(user)
                .on(paymentHistory.managerId.eq(user.userId))
                .where(paymentHistory.depositDate.between(startDateTime, endDateTime)
                        .and(user.userId.eq(userId)))
                .fetch();
    }

    @Override
    public long countPaidStudentsForUserInMonth(String userId, YearMonth yearMonth) {

        // 월의 첫째 날
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay(); // 해당 월의 첫 날 자정
        // 월의 마지막 날
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999); // 해당 월의 마지막 날 23시 59분 59초 999999999나노초

        return queryFactory
                .select(studentPaymentHistory.student.id)
                .from(studentPaymentHistory)
                .join(studentPaymentHistory.paymentHistory, paymentHistory)
                .join(studentPaymentHistory.student, student)
                .join(student.studentGroups, studentGroup)
                .join(studentGroup.group, group)
                .where(group.managerId.eq(userId)
                        .and(paymentHistory.depositDate.between(startDateTime, endDateTime))
                        .and(paymentHistory.paymentStatus.eq(PaymentStatus.PAID)))
                .fetch().size();
    }

    @Override
    @Transactional
    public long deleteByUserIdAndYearMonth(String userId, YearMonth yearMonth) {
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999);

        return queryFactory
                .delete(paymentHistory)
                .where(paymentHistory.managerId.eq(userId)
                        .and(paymentHistory.depositDate.between(startDateTime, endDateTime)))
                .execute();
    }

    @Override
    public Optional<PaymentHistory> findByDepositDateAndDepositorNameAndBankNameAndManagerId(LocalDateTime depositDate, String depositorName, String bankName, String userId) {

        // depositorName의 마지막 네 자리 숫자 및 공백을 빈 문자열로 제거하는 SQL 템플릿 정의
        // "(REGEXP_REPLACE({0}, '\\s*[0-9]{4}$', '')", paymentHistory.depositorName) 의 경우 {4}를 인덱스로 인식하여 오류 발생.
        // 따라서 {4} 대신 [0-9]를 네 번 반복하여 사용해야 함
        StringExpression depositorNameExpression = Expressions.stringTemplate(
                "REGEXP_REPLACE({0}, '\\s*[0-9][0-9][0-9][0-9]$', '')", paymentHistory.depositorName);

        BooleanExpression predicate = paymentHistory.depositDate.eq(depositDate)
                .and(paymentHistory.bankName.eq(bankName))
                .and(depositorNameExpression.eq(depositorName))
                .and(user.userId.eq(userId));

        PaymentHistory result = queryFactory
                .selectFrom(paymentHistory)
                .join(user)
                .on(paymentHistory.managerId.eq(user.userId))
                .where(predicate)
                .fetchOne();

        return Optional.ofNullable(result);
    }
}