package com.edubill.edubillApi.repository.payment;

import com.edubill.edubillApi.domain.PaymentHistory;
import com.edubill.edubillApi.domain.PaymentStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import static com.edubill.edubillApi.domain.QPaymentHistory.paymentHistory;
import static com.edubill.edubillApi.domain.QStudentGroup.studentGroup;
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
                .and(studentGroup.managerId.eq(userId));

        // 전체 카운트를 위한 쿼리
        long total = queryFactory
                .selectFrom(paymentHistory)
                .innerJoin(studentGroup)
                .on(paymentHistory.studentGroupId.eq(studentGroup.id))
                .where(predicate)
                .fetchCount();

        // 페이지 처리를 위한 결과 리스트
        List<PaymentHistory> results = queryFactory
                .selectFrom(paymentHistory)
                .innerJoin(studentGroup)
                .on(paymentHistory.studentGroupId.eq(studentGroup.id))
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
    public List<PaymentHistory> findPaymentHistoriesByYearMonthAndManagerId(String managerId, YearMonth yearMonth) {
        // 월의 첫째 날
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay(); // 해당 월의 첫 날 자정
        // 월의 마지막 날
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999); // 해당 월의 마지막 날 23시 59분 59초 999999999나노초

        return queryFactory
                .selectFrom(paymentHistory)
                .join(studentGroup)
                .on(paymentHistory.studentGroupId.eq(studentGroup.id))
                .where(paymentHistory.depositDate.between(startDateTime, endDateTime)
                        .and(studentGroup.managerId.eq(managerId))
                        .and(paymentHistory.paymentStatus.eq(PaymentStatus.PAID))) //TODO: StudentGroupId 존재유무만으로 납부완료리스트 가져올 수 있으면 제거
                .fetch();
    }

    @Override
    public List<PaymentHistory> findPaymentHistoriesWithUserIdAndYearMonth(String managerId, YearMonth yearMonth){

        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999);

        return queryFactory
                .selectFrom(paymentHistory)
                .join(user)
                .on(paymentHistory.managerId.eq(user.userId))
                .where(paymentHistory.depositDate.between(startDateTime, endDateTime)
                        .and(user.userId.eq(managerId)))
                .fetch();
    }

    @Override
    public long countPaidUserGroupsForUserInMonth(String managerId, YearMonth yearMonth) {

        // 월의 첫째 날
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay(); // 해당 월의 첫 날 자정
        // 월의 마지막 날
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999); // 해당 월의 마지막 날 23시 59분 59초 999999999나노초

        return queryFactory
                .selectFrom(studentGroup)
                .join(paymentHistory)
                .on(paymentHistory.studentGroupId.eq(studentGroup.id)
                        .and(paymentHistory.depositDate.between(startDateTime, endDateTime)))
                .where(studentGroup.managerId.eq(managerId))
                .fetchCount();
    }
}