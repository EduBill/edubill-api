package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.*;
import com.edubill.edubillApi.dto.payment.*;
import com.edubill.edubillApi.error.exception.PaymentHistoryNotFoundException;
import com.edubill.edubillApi.dto.payment.PaymentHistoryDetailResponse;

import com.edubill.edubillApi.dto.payment.PaymentHistoryResponse;
import com.edubill.edubillApi.dto.payment.PaymentStatusDto;
import com.edubill.edubillApi.error.exception.UserNotFoundException;
import com.edubill.edubillApi.repository.StudentPaymentRepository;
import com.edubill.edubillApi.repository.payment.PaymentHistoryRepository;
import com.edubill.edubillApi.repository.payment.PaymentKeyRepository;
import com.edubill.edubillApi.repository.student.StudentRepository;
import com.edubill.edubillApi.repository.studentgroup.StudentGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.YearMonth;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentHistoryRepository paymentHistoryRepository;
    private final PaymentKeyRepository paymentKeyRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final StudentRepository studentRepository;
    private final StudentPaymentRepository studentPaymentRepository;

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

    public Page<PaymentHistoryResponse> getUnpaidHistoriesForManagerInMonth(String userId, YearMonth yearMonth, Pageable pageable) {
        final Page<PaymentHistory> paymentHistories = paymentHistoryRepository.findUnpaidHistoriesByYearMonthAndManagerId(userId, yearMonth, pageable);

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
    public void generatePaymentKeys(YearMonth yearMonth, String userId) {
        //paymentHistory에 userId를 추가하여 외래키로 가지고 있음.
        List<PaymentHistory> paymentHistories = paymentHistoryRepository.findPaymentHistoriesWithUserIdAndYearMonth(userId, yearMonth);
        List<StudentGroup> studentGroups = studentGroupRepository.getStudentGroupsByUserId(userId);
        List<Student> students = new ArrayList<>();

        for (StudentGroup studentGroup : studentGroups) {
            students.addAll(studentRepository.findAllByStudentGroup(studentGroup));
        }

        // 학생을 이름별로 그룹화
        Map<String, List<Student>> studentsGroupedByName = students.stream()
                .collect(Collectors.groupingBy(Student::getStudentName));

        // 동명이인이 있는 학생과 없는 학생을 분리
        List<Student> uniqueStudents = new ArrayList<>();
        List<Student> duplicateNameStudents = new ArrayList<>();

        for (Map.Entry<String, List<Student>> entry : studentsGroupedByName.entrySet()) {
            List<Student> groupedStudents = entry.getValue();
            if (groupedStudents.size() == 1) {
                uniqueStudents.addAll(groupedStudents);
            } else {
                duplicateNameStudents.addAll(groupedStudents);
            }
        }

        // 유니크한 학생에 대한 처리
        for (Student student : uniqueStudents) {
            for (PaymentHistory paymentHistory : paymentHistories) {
                processPaymentKeyGeneral(student, paymentHistory);
            }
        }

        // 동명이인이 있는 학생에 대한 처리
        for (Student student : duplicateNameStudents) {
            for (PaymentHistory paymentHistory : paymentHistories) {
                processPaymentKeyDuplicate(student, paymentHistory);
            }
        }
    }

    private void processPaymentKeyDuplicate(Student student, PaymentHistory paymentHistory) {

    }


    private void processPaymentKeyGeneral(Student student, PaymentHistory paymentHistory) {
        String studentPhoneNumber = student.getStudentPhoneNumber();
        int tuition = student.getStudentGroup().getTuition();
        String studentName = student.getStudentName();
        String parentName = student.getParentName();
        String depositorName = paymentHistory.getDepositorName();
        String bankCode = BankName.getBankCodeByName(paymentHistory.getBankName());

        String newPaymentKey = depositorName + studentPhoneNumber + tuition + bankCode + paymentHistory.getPaymentType();

        List<PaymentKey> paymentKeys = paymentKeyRepository.findAllByStudent(student);
        // case1: 결제키가 존재하는 경우
        if (paymentKeys != null && !paymentKeys.isEmpty()) {
            for (PaymentKey paymentKey : paymentKeys) {
                if (paymentKey == null) {
                    log.error("Null paymentKey encountered");
                } else if (paymentKey.matches(newPaymentKey)) {
                    paymentStatusToPaid(student, paymentHistory);
                    break;
                } else {
                    paymentStatusToUnPaid(paymentHistory);
                }
            }
        } else {
            // case2: 결제 키가 아예 존재하지 않는 경우
            if (depositorName.equals(studentName) || depositorName.equals(parentName)) { //TODO: depositorName == parentName 제거 필요
                paymentStatusToPaid(student, paymentHistory);
                paymentKeyRepository.save(PaymentKey.builder()
                        .paymentKey(newPaymentKey)
                        .student(student)
                        .build());
            }
        }
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

    private void paymentStatusToUnPaid(PaymentHistory paymentHistory) {
        paymentHistoryRepository.save(paymentHistory.toBuilder()
                .paymentStatus(PaymentStatus.UNPAID) //결제 미완료상태로 변경
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

    /*
        Func : 미납내역 수동처리 -> 미납 학생 선택한 미확인 입금내역과 연결하여 완납처리
        Parameter : 미납 학생 ID, 미확인 입금 내역 ID
        Return : void
    */
    @Transactional
    public void manualProcessingOfUnpaidHistory(Long studentId, Long paymentHistoryId, String yearMonth){
        // id로 미납 학생 찾기
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다.  userId: "+ studentId));

        // id로 미확인 입금 내역 찾기
        PaymentHistory paymentHistory = paymentHistoryRepository.findById(paymentHistoryId)
                .orElseThrow(() -> new PaymentHistoryNotFoundException("존재하지 않는 납부 내역입니다."));

        int tuition = student.getStudentGroup().getTuition();
        String studentPhoneNumber = student.getStudentPhoneNumber();
        String depositorName = paymentHistory.getDepositorName();
        String bankCode = BankName.getBankCodeByName(paymentHistory.getBankName());

        // 새로운 결제키 생성 -> 이전에 수동처리한 적 없으니 결제키 존재한 적 X
        String newPaymentKey = depositorName + studentPhoneNumber + tuition + bankCode + paymentHistory.getPaymentType();

        // 결제키 암호화
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedString = encoder.encodeToString(newPaymentKey.getBytes());

        // 완납처리 -> 납입기록은 이때 납입완료로 처리
        paymentStatusToPaid(student, paymentHistory);

        // 미납 리스트 학생에서 해당 학생이 조회되지 않도록 처리
        StudentPaymentHistory studentPaymentHistory = StudentPaymentHistory.builder()
                .student(student)
                .paymentHistory(paymentHistory)
                .yearMonth(yearMonth)
                .build();

        studentPaymentRepository.save(studentPaymentHistory);


        // 결제키 저장
        paymentKeyRepository.save(PaymentKey.builder()
                .paymentKey(encodedString)
                .student(student)
                .build());
    }
}
