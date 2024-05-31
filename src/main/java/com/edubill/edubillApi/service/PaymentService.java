package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.*;
import com.edubill.edubillApi.dto.payment.*;
import com.edubill.edubillApi.error.exception.PaymentHistoryNotFoundException;
import com.edubill.edubillApi.dto.payment.PaymentHistoryDetailResponse;
import com.edubill.edubillApi.dto.payment.PaymentHistoryDto;

import com.edubill.edubillApi.excel.ExcelService;
import com.edubill.edubillApi.repository.*;
import com.edubill.edubillApi.dto.payment.PaymentHistoryResponse;
import com.edubill.edubillApi.dto.payment.PaymentStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.YearMonth;

import java.util.List;
import java.util.Optional;

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


        return PaymentStatusDto.builder()
                .paidCount(paidStudentsCountInMonth)
                .unpaidCount(unpaidStudentsCount)
                .totalPaidAmount(totalPaidAmount)
                .totalUnpaidAmount(totalUnpaidAmount)
                .build();
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

    public MemoResponseDto updateMemo(MemoRequestDto memoRequestDto) {
        Long paymentHistoryId = memoRequestDto.getPaymentHistoryId();
        PaymentHistory paymentHistory = paymentHistoryRepository.findById(paymentHistoryId)
                .orElseThrow(() -> new PaymentHistoryNotFoundException("납부내역 없음"));

        PaymentHistory updatedPaymentHistory = paymentHistoryRepository.save(paymentHistory.toBuilder()
                .memo(memoRequestDto.getMemoDescription())
                .build());

        return MemoResponseDto.builder()
                .memoDescription(updatedPaymentHistory.getMemo())
                .build();
    }
}
