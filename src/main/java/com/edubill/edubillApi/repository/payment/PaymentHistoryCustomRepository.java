package com.edubill.edubillApi.repository.payment;

import com.edubill.edubillApi.domain.PaymentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;
import java.util.List;

public interface PaymentHistoryCustomRepository {

    Page<PaymentHistory> findPaymentHistoriesByYearMonthAndManagerId(String userId, YearMonth yearMonth, Pageable pageable);

    List<PaymentHistory> findPaymentHistoriesByYearMonthAndManagerId(String managerId, YearMonth yearMonth);

    /**
     * PaymentHistory의 student_group_id가 null인 케이스를 전부 포함하여 가져온다.
     */
    List<PaymentHistory> findPaymentHistoriesWithUserId(String managerId, YearMonth yearMonth);

    long countPaidUserGroupsForUserInMonth(String userId, YearMonth yearMonth);
}
