package com.edubill.edubillApi.service;

import com.edubill.edubillApi.error.exception.PaymentHistoryNotFoundException;
import com.edubill.edubillApi.domain.PaymentType;
import com.edubill.edubillApi.dto.payment.PaymentHistoryDetailResponse;
import com.edubill.edubillApi.dto.payment.PaymentHistoryDto;
import com.edubill.edubillApi.domain.PaymentHistory;

import com.edubill.edubillApi.domain.StudentGroup;
import com.edubill.edubillApi.repository.PaymentHistoryRepository;
import com.edubill.edubillApi.dto.payment.PaymentHistoryResponse;
import com.edubill.edubillApi.dto.payment.PaymentStatusDto;
import com.edubill.edubillApi.repository.StudentGroupRepository;
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

    public void savePaymentHistories(List<PaymentHistory> paymentHistories) {
        paymentHistoryRepository.saveAll(paymentHistories);
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
                new PaymentHistoryResponse(paymentHistory.getId(),paymentHistory.getDepositorName(), paymentHistory.getPaidAmount(), paymentHistory.getDepositDate())
        );
    }

    public PaymentHistory mapToPaymentHistoryWithStudentGroup(PaymentHistoryDto paymentHistoryDto, String userId) {

        List<StudentGroup> studentGroups = studentGroupRepository.getUserGroupsByUserId(userId);

        if (studentGroups != null && !studentGroups.isEmpty()) {
            //TODO: StudentGroupId에 대한 정보를 받고 해당하는 Id에 따라 paymentHistory를 저장하도록 수정
            StudentGroup studentGroup = studentGroups.get(0);
            PaymentHistory paymentHistory = PaymentHistory.toEntity(paymentHistoryDto, studentGroup.getId(), PaymentType.BANK_TRANSFER);

            return paymentHistory;

        } else {
            StudentGroup newStudentGroup = StudentGroup.builder()
                    .groupName("중등 A반")
                    .managerId(userId)
                    .tuition(300000)
                    .totalStudentCount(20)
                    .build();
            studentGroupRepository.save(newStudentGroup);

            PaymentHistory paymentHistory = PaymentHistory.toEntity(paymentHistoryDto, newStudentGroup.getId(), PaymentType.BANK_TRANSFER);
            return paymentHistory;
        }
    }

    public PaymentHistoryDetailResponse findPaymentHistoryById(long paymentHistoryId) {

        PaymentHistory paymentHistory = paymentHistoryRepository.findById(paymentHistoryId)
                .orElseThrow(()-> new PaymentHistoryNotFoundException("납부내역 없음"));
        return PaymentHistoryDetailResponse.of(paymentHistory);
    }
}
