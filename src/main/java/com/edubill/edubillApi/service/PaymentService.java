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
        List<StudentGroup> studentGroups = studentGroupRepository.getStudentGroupsByUserId(managerId);

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
                new PaymentHistoryResponse(paymentHistory.getId(), paymentHistory.getDepositorName(), paymentHistory.getPaidAmount(), paymentHistory.getDepositDate())
        );
    }

    public PaymentHistoryDetailResponse findPaymentHistoryById(long paymentHistoryId) {

        PaymentHistory paymentHistory = paymentHistoryRepository.findById(paymentHistoryId)
                .orElseThrow(() -> new PaymentHistoryNotFoundException("납부내역 없음"));
        return PaymentHistoryDetailResponse.of(paymentHistory);
    }

    @Transactional
    public void generatePaymentKeysAndSetPaymentStatus(YearMonth yearMonth, String userId) {

        List<PaymentHistory> paymentHistoriesByYearMonth = paymentHistoryRepository.findPaymentHistoriesByYearMonthAndManagerId(userId, yearMonth);
        List<StudentGroup> studentGroups = studentGroupRepository.getStudentGroupsByUserId(userId);
        List<Student> students;

        for (StudentGroup studentGroup : studentGroups) {
            students = studentRepository.findAllByStudentGroup(studentGroup);

            for (Student student : students) {

                for (PaymentHistory paymentHistory : paymentHistoriesByYearMonth) {
                    String depositorName = paymentHistory.getDepositorName();

                    if (depositorName.equals(student.getStudentName()) || depositorName.equals(student.getParentName())) {
                        //입금자이름이 학생이고 중복되는 학생이름이 없는경우 ---> 중요
                        paymentHistoryRepository.save(paymentHistory.toBuilder()
                                .studentGroupId(student.getStudentGroup().getId()) //학원반 연관관계 설정
                                .paymentStatus(PaymentStatus.PAID) //결제완료상태로 변경
                                .build());
                        //결제키 생성로직(해당학생이름,  해당학생연락처, 입금금액, 은행코드, 결제방식)
                        String paymentKey = student.getStudentName() + student.getStudentPhoneNumber() + paymentHistory.getPaidAmount() + paymentHistory.getPaymentType() + paymentHistory.getBankName();

                    } else {
                        //입금자이름이 학부모이름일때
                        paymentHistoryRepository.save(paymentHistory.toBuilder()
                                .paymentStatus(PaymentStatus.UNPAID) //결제완료상태로 변경
                                .build());

                    }
                }
            }
        }
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
