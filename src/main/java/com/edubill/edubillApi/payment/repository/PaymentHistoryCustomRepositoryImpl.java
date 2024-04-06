package com.edubill.edubillApi.payment.repository;

import com.edubill.edubillApi.payment.domain.PaymentHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static com.edubill.edubillApi.domain.QStudentGroup.studentGroup;
import static com.edubill.edubillApi.payment.domain.QPaymentHistory.paymentHistory;

@Repository
public class PaymentHistoryCustomRepositoryImpl implements PaymentHistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    public PaymentHistoryCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<PaymentHistory> findPaymentHistoriesByYearMonthAndManagerId(String managerId, YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return queryFactory
                .selectFrom(paymentHistory)
                .join(studentGroup)
                .on(paymentHistory.studentGroupId.eq(studentGroup.id))
                .where(paymentHistory.depositDate.between(startDate, endDate)
                        .and(studentGroup.managerId.eq(managerId)))
                .fetch();
    }

    @Override
    public long countPaidUserGroupsForUserInMonth(String managerId, YearMonth yearMonth) {

        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        return queryFactory
                .selectFrom(studentGroup)
                .join(paymentHistory)
                .on(paymentHistory.studentGroupId.eq(studentGroup.id)
                        .and(paymentHistory.depositDate.between(startOfMonth, endOfMonth)))
                .where(studentGroup.managerId.eq(managerId))
                .fetchCount();
    }
}