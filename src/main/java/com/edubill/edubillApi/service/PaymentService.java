package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.*;
import com.edubill.edubillApi.domain.enums.PaymentStatus;
import com.edubill.edubillApi.domain.enums.PaymentType;
import com.edubill.edubillApi.dto.FileUrlResponseDto;
import com.edubill.edubillApi.dto.payment.*;
import com.edubill.edubillApi.error.ErrorCode;
import com.edubill.edubillApi.error.exception.BusinessException;
import com.edubill.edubillApi.error.exception.PaymentHistoryNotFoundException;
import com.edubill.edubillApi.dto.payment.PaymentHistoryDetailResponse;

import com.edubill.edubillApi.dto.payment.PaymentHistoryResponseDto;
import com.edubill.edubillApi.dto.payment.PaymentStatusDto;
import com.edubill.edubillApi.error.exception.PaymentKeyNotEncryptedException;
import com.edubill.edubillApi.error.exception.UserNotFoundException;
import com.edubill.edubillApi.repository.StudentPaymentHistoryRepository;
import com.edubill.edubillApi.repository.payment.PaymentHistoryRepository;

import com.edubill.edubillApi.repository.payment.PaymentKeyRepository;
import com.edubill.edubillApi.repository.student.StudentRepository;
import com.edubill.edubillApi.repository.group.GroupRepository;
import com.edubill.edubillApi.utils.EncryptionUtils;
import com.edubill.edubillApi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentHistoryRepository paymentHistoryRepository;
    private final PaymentKeyRepository paymentKeyRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final StudentPaymentHistoryRepository studentPaymentHistoryRepository;
    private final FileUploadService fileUploadService;

    @Value("${payment.secret.key}")
    private String SECRET_KEY; // 16-byte key for AES

    @Transactional
    public void savePaymentHistories(List<PaymentHistory> paymentHistories) {
        final String managerId = SecurityUtils.getCurrentUserId();

        for (PaymentHistory paymentHistory : paymentHistories) {
            Optional<PaymentHistory> existingPaymentHistory = paymentHistoryRepository.findByDepositDateAndDepositorNameAndBankNameAndManagerId(paymentHistory.getDepositDate(), paymentHistory.getDepositorName(), paymentHistory.getBankName(), managerId);

            if (existingPaymentHistory.isPresent()) {
                PaymentHistory existing = existingPaymentHistory.get();
                // 변경사항 추가
                paymentHistoryRepository.save(existing);
            } else {
                paymentHistoryRepository.save(paymentHistory);
            }
        }
    }

    @Transactional
    public PaymentStatusDto getPaymentStatusForManagerInMonth(final String managerId, final YearMonth yearMonth) {
        final long paidStudentsCountInMonth = paymentHistoryRepository.countPaidStudentsForUserInMonth(managerId, yearMonth);

        List<PaymentHistory> paymentHistories = paymentHistoryRepository.findPaymentHistoriesByYearMonthAndManagerId(managerId, yearMonth);
        List<Group> groups = groupRepository.getGroupsByUserId(managerId);

        final long totalNumberOfStudentsToPay = groups.stream()
                .mapToInt(Group::getTotalStudentCount)
                .sum();

        final long unpaidStudentsCount = totalNumberOfStudentsToPay - paidStudentsCountInMonth;

        final long totalPaidAmount = paymentHistories.stream().mapToInt(PaymentHistory::getPaidAmount).sum();

        final long totalTuition = groups.stream()
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

    public Page<PaymentHistoryResponseDto> getPaidHistoriesForManagerInMonth(String userId, YearMonth yearMonth, Pageable pageable) {
        final Page<PaymentHistory> paymentHistories = paymentHistoryRepository.findPaymentHistoriesByYearMonthAndManagerId(userId, yearMonth, pageable);

        return paymentHistories.map(paymentHistory ->
                new PaymentHistoryResponseDto(paymentHistory.getId(), paymentHistory.getDepositorName(), paymentHistory.getPaidAmount(), paymentHistory.getDepositDate())
        );
    }

    public Page<PaymentHistoryResponseDto> getUnpaidHistoriesForManagerInMonth(String userId, YearMonth yearMonth, Pageable pageable) {
        final Page<PaymentHistory> paymentHistories = paymentHistoryRepository.findUnpaidHistoriesByYearMonthAndManagerId(userId, yearMonth, pageable);

        return paymentHistories.map(paymentHistory ->
                new PaymentHistoryResponseDto(paymentHistory.getId(), paymentHistory.getDepositorName(), paymentHistory.getPaidAmount(), paymentHistory.getDepositDate())
        );
    }

    public PaymentHistoryDetailResponse findPaymentHistoryById(long paymentHistoryId) {

        PaymentHistory paymentHistory = paymentHistoryRepository.findById(paymentHistoryId)
                .orElseThrow(() -> new PaymentHistoryNotFoundException("납부내역 없음"));
        return PaymentHistoryDetailResponse.of(paymentHistory);
    }

    @Transactional
    public void handleStudentPaymentProcessing(YearMonth yearMonth, String userId) {
        //paymentHistory 에 userId를 추가하여 외래키로 가지고 있음.
        List<PaymentHistory> paymentHistories = paymentHistoryRepository.findPaymentHistoriesWithUserIdAndYearMonth(userId, yearMonth);
        List<Group> groups = groupRepository.getGroupsByUserId(userId);

        // 중복된 이름을 가진 학생들 가져오기
        List<Student> duplicateNameStudents = studentRepository.findStudentsWithDuplicateNames(groups);
        // 유니크한 이름을 가진 학생들 가져오기
        List<Student> uniqueStudents = studentRepository.findStudentsWithUniqueNames(groups);

        // 유니크한 학생에 대한 처리
        for (Student student : uniqueStudents) {
            boolean isPaymentProcessed;
            for (PaymentHistory paymentHistory : paymentHistories) {
                if (paymentHistory.getPaymentStatus().equals(PaymentStatus.UNPAID)) {
                    isPaymentProcessed = processPaymentKeyGeneral(student, paymentHistory, yearMonth);
                    if (isPaymentProcessed) {
                        break;
                    }
                }
            }
        }

        // 동명이인이 있는 학생에 대한 처리
        for (Student student : duplicateNameStudents) {
            boolean isPaymentProcessed;
            for (PaymentHistory paymentHistory : paymentHistories) {
                if (paymentHistory.getPaymentStatus().equals(PaymentStatus.UNPAID)) {
                    isPaymentProcessed = processPaymentKeyDuplicate(student, paymentHistory, yearMonth);
                    if (isPaymentProcessed) {
                        break;
                    }
                }
            }
        }
    }

    // 동명이인이 존재하지 않는 케이스
    private boolean processPaymentKeyGeneral(Student student, PaymentHistory paymentHistory, YearMonth yearMonth) {
        String studentPhoneNumber = student.getStudentPhoneNumber();
        String studentName = student.getStudentName();
        String depositorName = paymentHistory.getDepositorName();
        Integer paidAmount = paymentHistory.getPaidAmount();

        if (!isPaidAmountMatching(student, paidAmount)) {
            return false;
        }

        String newPaymentKey = depositorName + studentPhoneNumber + paidAmount + paymentHistory.getPaymentType();
        String encryptedNewPaymentKey;

        try {
            encryptedNewPaymentKey = EncryptionUtils.encrypt(newPaymentKey, SECRET_KEY);
        } catch (Exception e) {
            throw new PaymentKeyNotEncryptedException("암호할 할 수 없습니다.");
        }

        List<PaymentKey> paymentKeys = paymentKeyRepository.findAllByStudent(student);
        // case1: 결제키가 존재하는 경우
        if (paymentKeys != null && !paymentKeys.isEmpty()) {
            for (PaymentKey paymentKey : paymentKeys) {
                if (paymentKey == null) {
                    log.error("Null paymentKey encountered");
                } else if (paymentKey.matches(encryptedNewPaymentKey)) {
                    paymentStatusToPaid(student, paymentHistory);
                    createStudentPaymentHistory(student, paymentHistory, yearMonth);
                    return true;  // 결제 상태가 PAID로 변경되었음을 반환
                } else {
                    //TODO: 한 학생이 두개의 반에 속할때 두 반의 학원비가 다를 경우
                    paymentStatusToUnPaid(paymentHistory);
                }
            }
        } else {
            // case2: 결제 키가 아예 존재하지 않는 경우
            if (depositorName.equals(studentName)) {
                paymentStatusToPaid(student, paymentHistory);
                createStudentPaymentHistory(student, paymentHistory, yearMonth);
                paymentKeyRepository.save(PaymentKey.builder()
                        .paymentKey(encryptedNewPaymentKey)
                        .student(student)
                        .build());
                return true;  // 결제 상태가 PAID로 변경되었음을 반환
            }
        }
        return false;  // 결제 상태가 변경되지 않음
    }

    // 동명이인이 존재하는 케이스
    private Boolean processPaymentKeyDuplicate(Student student, PaymentHistory paymentHistory, YearMonth yearMonth) {
        String studentPhoneNumber = student.getStudentPhoneNumber();
        String depositorName = paymentHistory.getDepositorName();
        Integer paidAmount = paymentHistory.getPaidAmount();

        if (!isPaidAmountMatching(student, paidAmount)) {
            return false;
        }

        //홍길동1111 300000 004 BANK_TRANSFER
        String newPaymentKey = depositorName + studentPhoneNumber + paidAmount + paymentHistory.getPaymentType();
        String encryptedNewPaymentKey;

        try {
            encryptedNewPaymentKey = EncryptionUtils.encrypt(newPaymentKey, SECRET_KEY);
        } catch (Exception e) {
            throw new PaymentKeyNotEncryptedException("암호할 할 수 없습니다.");
        }

        List<PaymentKey> paymentKeys = paymentKeyRepository.findAllByStudent(student);
        // case1: 결제키가 존재하는 경우
        if (paymentKeys != null && !paymentKeys.isEmpty()) {
            for (PaymentKey paymentKey : paymentKeys) {
                if (paymentKey.matches(encryptedNewPaymentKey)) {
                    paymentStatusToPaid(student, paymentHistory);
                    createStudentPaymentHistory(student, paymentHistory, yearMonth);
                    return true;
                } else {
                    paymentStatusToUnPaid(paymentHistory);
                    return false;
                }
            }
        } else {
            // case2: 결제 키가 아예 존재하지 않는 경우
            paymentStatusToUnPaid(paymentHistory);
        }
        return true;
    }

    /*
        Func : 미납내역 수동처리 -> 미납 학생 선택한 미확인 입금내역과 연결하여 완납처리
        Parameter : 미납 학생 ID, 미확인 입금 내역 ID
        Return : void
    */
    @Transactional
    public void manualProcessingOfUnpaidHistory(Long studentId, Long paymentHistoryId, YearMonth yearMonth) {
        // id로 미납 학생 찾기
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다.  userId: " + studentId));

        // id로 미확인 입금 내역 찾기
        PaymentHistory paymentHistory = paymentHistoryRepository.findById(paymentHistoryId)
                .orElseThrow(() -> new PaymentHistoryNotFoundException("존재하지 않는 납부 내역입니다."));

        String studentPhoneNumber = student.getStudentPhoneNumber();
        String depositorName = paymentHistory.getDepositorName();
        Integer paidAmount = paymentHistory.getPaidAmount();

        if (!isPaidAmountMatching(student, paidAmount)) {
            throw new BusinessException("납부금액과 학원비가 일치하지 않습니다.", ErrorCode.PAID_AMOUNT_TUITION_MISMATCH);
        }

        // 새로운 결제키 생성 -> 이전에 수동처리한 적 없으니 결제키 존재한 적 X
        String newPaymentKey = depositorName + studentPhoneNumber + paidAmount + paymentHistory.getPaymentType();
        String encryptedNewPaymentKey;

        try {
            encryptedNewPaymentKey = EncryptionUtils.encrypt(newPaymentKey, SECRET_KEY);
        } catch (Exception e) {
            throw new PaymentKeyNotEncryptedException("암호할 할 수 없습니다.");
        }

        // 완납처리 -> 납입기록은 이때 납입완료로 처리
        paymentStatusToPaid(student, paymentHistory);

        // 미납 리스트 학생에서 해당 학생이 조회되지 않도록 처리
        createStudentPaymentHistory(student, paymentHistory, yearMonth);

        // 결제키 저장
        paymentKeyRepository.save(PaymentKey.builder()
                .paymentKey(encryptedNewPaymentKey)
                .student(student)
                .build());
    }

    @Transactional
    public FileUrlResponseDto manualProcessingOfUnpaidHistoryByManualInput(ManualPaymentHistoryRequestDto manualPaymentHistoryRequestDto) throws IOException {
        String userId = SecurityUtils.getCurrentUserId();
        Long studentId = manualPaymentHistoryRequestDto.getStudentId();
        PaymentType paymentType = PaymentType.getPaymentTypeByDescription(manualPaymentHistoryRequestDto.getPaymentTypeString());

        // YearMonth와 임의의 시간을 사용하여 LocalDateTime 생성
        YearMonth yearMonth = manualPaymentHistoryRequestDto.getYearMonth();
        LocalTime arbitraryTime = LocalTime.of(12, 0); // 임의의 시간 설정 (여기서는 12:00로 설정)
        LocalDateTime depositDate = yearMonth.atDay(1).atTime(arbitraryTime);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다.  userId: " + studentId));

        String s3Url = fileUploadService.saveImageFile(manualPaymentHistoryRequestDto.getFile());

        PaymentHistory newPaymentHistory = paymentHistoryRepository.save(PaymentHistory.builder()
                .depositDate(depositDate)
                .bankName("수동입력")
                .paidAmount(manualPaymentHistoryRequestDto.getPaidAmount())
                .memo(manualPaymentHistoryRequestDto.getMemo())
                .paymentType(paymentType)
                .managerId(userId)
                .s3Url(s3Url)
                .build());


        paymentStatusToPaid(student, newPaymentHistory);
        createStudentPaymentHistory(student, newPaymentHistory, yearMonth);

        if (!isPaidAmountMatching(student, newPaymentHistory.getPaidAmount())) {
            throw new BusinessException("납부금액과 학원비가 일치하지 않습니다.", ErrorCode.PAID_AMOUNT_TUITION_MISMATCH);
        }


        String newPaymentKey = student.getStudentName() + student.getStudentPhoneNumber() + newPaymentHistory.getPaidAmount() + paymentType;
        String encryptedNewPaymentKey;
        try {
            encryptedNewPaymentKey = EncryptionUtils.encrypt(newPaymentKey, SECRET_KEY);
        } catch (Exception e) {
            throw new PaymentKeyNotEncryptedException("암호할 할 수 없습니다.");
        }
        // 결제키 저장
        paymentKeyRepository.save(PaymentKey.builder()
                .paymentKey(encryptedNewPaymentKey)
                .student(student)
                .build());

        return new FileUrlResponseDto(s3Url);
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

    private void paymentStatusToPaid(Student student, PaymentHistory paymentHistory) {
        String studentPhoneNumber = student.getStudentPhoneNumber();
        String lastFourDigits = studentPhoneNumber.substring(studentPhoneNumber.length() - 4);
        String modifiedStudentName = student.getStudentName() + " " + lastFourDigits;

        paymentHistoryRepository.save(paymentHistory.toBuilder()
                .depositorName(modifiedStudentName)
                .paymentStatus(PaymentStatus.PAID) //결제완료상태로 변경
                .build());
    }

    private void paymentStatusToUnPaid(PaymentHistory paymentHistory) {
        paymentHistoryRepository.save(paymentHistory.toBuilder()
                .paymentStatus(PaymentStatus.UNPAID) //결제 미완료상태로 변경
                .build());
    }

    private void createStudentPaymentHistory(Student student, PaymentHistory paymentHistory, YearMonth yearMonth) {
        String yearMonthString = yearMonth.toString();

        StudentPaymentHistory studentPaymentHistory = StudentPaymentHistory.builder()
                .yearMonth(yearMonthString)
                .build();

        // Student와 PaymentHistory 간의 연관 관계 설정
        studentPaymentHistory.setStudent(student);
        studentPaymentHistory.setPaymentHistory(paymentHistory);

        studentPaymentHistoryRepository.save(studentPaymentHistory);
    }

    /*
        Func : 미납 학생 조회
        Parameter : userId, yearMonth
        Return : List<UnpaidStudentsResponseDto>
    */
    public List<UnpaidStudentsResponseDto> getUnpaidStudentsList(String userId, YearMonth yearMonth) {

        List<Student> unPaidStudents = studentRepository.findUnpaidStudentsByYearMonthAndManagerId(userId, yearMonth);

        return unPaidStudents.stream()
                .map(student -> {
                    String sPhoneNum = student.getStudentPhoneNumber();

                    return UnpaidStudentsResponseDto.builder()
                            .studentId(student.getId()) // 수동처리 시 학생Id를 프론트에서 받기 위함
                            .studentName(student.getStudentName() + " " + sPhoneNum.substring(sPhoneNum.length() - 4)) // 홍길동 1234
                            .build();
                })
                .collect(Collectors.toList());


    }

    private static boolean isPaidAmountMatching(Student student, Integer paidAmount) {
        // Retrieve all the tuition amounts from the student's groups and sum them
        int totalTuition = student.getStudentGroups().stream()
                .mapToInt(studentGroup -> studentGroup.getGroup().getTuition())
                .sum();

        // Check if paidAmount matches the total tuition amount
        return Objects.equals(totalTuition, paidAmount);
    }
}
