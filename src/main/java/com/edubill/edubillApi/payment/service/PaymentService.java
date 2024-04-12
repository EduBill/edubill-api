package com.edubill.edubillApi.payment.service;

import com.edubill.edubillApi.user.domain.StudentGroup;
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

        List<StudentGroup> studentGroups = studentGroupRepository.getUserGroupsByUserId(managerId);

        final long totalNumberOfStudentsToPay = studentGroups.stream()
                .mapToInt(StudentGroup::getTotalStudentCount)
                .sum();

        final long unpaidStudentsCount = totalNumberOfStudentsToPay - paidStudentsCountInMonth;

        return new PaymentStatusDto(paidStudentsCountInMonth, unpaidStudentsCount);
    }

}
