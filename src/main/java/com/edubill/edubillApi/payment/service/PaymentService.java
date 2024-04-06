package com.edubill.edubillApi.payment.service;

import com.edubill.edubillApi.payment.domain.PaymentHistory;
import com.edubill.edubillApi.payment.repository.PaymentHistoryRepository;
import com.edubill.edubillApi.payment.response.PaymentStatusDto;
import com.edubill.edubillApi.user.repository.StudentGroupRepository;
import org.springframework.stereotype.Service;

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

    public PaymentStatusDto getPaymentStatusForManagerInMonth(final String managerId, final YearMonth yearMonth) {
        final long paidStudentsCountInMonth = paymentHistoryRepository.countPaidUserGroupsForUserInMonth(managerId, yearMonth);

        final long totalNumberOfStudentsToPay = studentGroupRepository.countUserGroupsByUserId(managerId);

        final long unpaidStudentsCount = totalNumberOfStudentsToPay - paidStudentsCountInMonth;

        return new PaymentStatusDto(paidStudentsCountInMonth, unpaidStudentsCount);
    }

}
