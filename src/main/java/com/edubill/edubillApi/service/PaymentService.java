package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.*;
import com.edubill.edubillApi.dto.payment.*;
import com.edubill.edubillApi.error.exception.PaymentHistoryNotFoundException;
import com.edubill.edubillApi.dto.payment.PaymentHistoryDetailResponse;

import com.edubill.edubillApi.dto.payment.PaymentHistoryResponse;
import com.edubill.edubillApi.dto.payment.PaymentStatusDto;
import com.edubill.edubillApi.repository.payment.PaymentHistoryRepository;
import com.edubill.edubillApi.repository.payment.PaymentKeyRepository;
import com.edubill.edubillApi.repository.student.StudentRepository;
import com.edubill.edubillApi.repository.studentgroup.StudentGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.YearMonth;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentHistoryRepository paymentHistoryRepository;
    private final PaymentKeyRepository paymentKeyRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final StudentRepository studentRepository;

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
        //paymentHistory에 userId를 추가하여 외래키로 가지고 있음.
        List<PaymentHistory> paymentHistories = paymentHistoryRepository.findPaymentHistoriesWithUserId(userId, yearMonth);

        List<StudentGroup> studentGroups = studentGroupRepository.getStudentGroupsByUserId(userId);
        List<Student> students;

        for (StudentGroup studentGroup : studentGroups) {
            students = studentRepository.findAllByStudentGroup(studentGroup);
            for (Student student : students) {
                for (PaymentHistory paymentHistory : paymentHistories) {
                    String depositorName = paymentHistory.getDepositorName();
                    String studentName = student.getStudentName();
                    String parentName = student.getParentName();

                    // 입금자명이 학생정보의 학생명이나 학부모명과 일치할 경우 해당학생을 입금확인처리를 합니다.
                    if (depositorName.equals(studentName)) {
                        paymentStatusToPaid(student, paymentHistory);
                        generatePaymentKeys(student, paymentHistory);
                        break;
                    } else if (depositorName.equals(parentName)) {
                        paymentStatusToPaid(student, paymentHistory);
                        generatePaymentKeys(student, paymentHistory);
                        break;
                    }
                    else{
                        paymentHistoryRepository.save(paymentHistory.toBuilder()
                                .paymentStatus(PaymentStatus.UNPAID) //결제 미완료상태로 변경
                                .build());
                    }
                }
            }
        }
    }

    private void generatePaymentKeys(Student student, PaymentHistory paymentHistory) {
        String studentPhoneNumber = student.getStudentPhoneNumber();
        String studentName = student.getStudentName();
        String paymentKey = studentName + studentPhoneNumber + paymentHistory.getPaidAmount() + paymentHistory.getPaymentType() + paymentHistory.getBankName();

        paymentKeyRepository.save(PaymentKey.builder()
                .paymentKey(paymentKey)
                .student(student)
                .build());
    }

    private void paymentStatusToPaid(Student student, PaymentHistory paymentHistory) {
        String studentPhoneNumber = student.getStudentPhoneNumber();
        String lastFourDigits = studentPhoneNumber.substring(studentPhoneNumber.length() - 4);
        String modifiedStudentName = student.getStudentName() + lastFourDigits;

        paymentHistoryRepository.save(paymentHistory.toBuilder()
                .depositorName(modifiedStudentName)
                .studentGroupId(student.getStudentGroup().getId()) //학원반 연관관계 설정
                .paymentStatus(PaymentStatus.PAID) //결제완료상태로 변경
                .build());
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
