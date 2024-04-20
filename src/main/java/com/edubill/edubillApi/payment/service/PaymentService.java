package com.edubill.edubillApi.payment.service;

import com.edubill.edubillApi.domain.StudentGroup;
import com.edubill.edubillApi.payment.domain.PaymentHistory;
import com.edubill.edubillApi.payment.repository.PaymentHistoryRepository;
import com.edubill.edubillApi.payment.response.PaymentHistoryResponse;
import com.edubill.edubillApi.payment.response.PaymentStatusDto;
import com.edubill.edubillApi.user.repository.StudentGroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentHistoryRepository paymentHistoryRepository;

    private final StudentGroupRepository studentGroupRepository;

    public PaymentService(PaymentHistoryRepository paymentHistoryRepository, StudentGroupRepository studentGroupRepository) {
        this.paymentHistoryRepository = paymentHistoryRepository;
        this.studentGroupRepository = studentGroupRepository;
    }

    @Transactional
    public PaymentStatusDto getPaymentStatusForManagerInMonth(final String managerId, final YearMonth yearMonth) {
        final long paidStudentsCountInMonth = paymentHistoryRepository.countPaidUserGroupsForUserInMonth(managerId, yearMonth);

        List<PaymentHistory> paymentHistories = paymentHistoryRepository.findPaymentHistoriesByYearMonthAndManagerId(managerId, yearMonth);

        List<StudentGroup> studentGroups = studentGroupRepository.getUserGroupsByUserId(managerId);

        final long totalNumberOfStudentsToPay = studentGroups.stream()
                .mapToInt(StudentGroup::getTotalStudentCount)
                .sum();

        final long unpaidStudentsCount = totalNumberOfStudentsToPay - paidStudentsCountInMonth;

        final long totalPaidAmount = paymentHistories.stream().mapToInt(PaymentHistory::getPaidAmount).sum();

        final long totalTuition = studentGroups.stream()
                .mapToInt(group -> group.getTuition() * group.getTotalStudentCount())
                .sum();

        final long totalUnpaidAmount = totalTuition - totalPaidAmount;

        return new PaymentStatusDto(paidStudentsCountInMonth, unpaidStudentsCount, totalPaidAmount, totalUnpaidAmount);
    }

    public Page<PaymentHistoryResponse> getPaidHistoriesForManagerInMonth(String userId, YearMonth yearMonth, Pageable pageable) {
        final Page<PaymentHistory> paymentHistories = paymentHistoryRepository.findPaymentHistoriesByYearMonthAndManagerId(userId, yearMonth, pageable);

        return paymentHistories.map(paymentHistory ->
            new PaymentHistoryResponse(paymentHistory.getDepositor(), paymentHistory.getPaidAmount(), paymentHistory.getDepositDate())
        );
    }
}
