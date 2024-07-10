package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.repository.StudentPaymentHistoryRepository;
import com.edubill.edubillApi.repository.payment.PaymentHistoryRepository;
import com.edubill.edubillApi.repository.payment.PaymentKeyCustomRepository;
import com.edubill.edubillApi.repository.payment.PaymentKeyRepository;
import com.edubill.edubillApi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/v1/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final PaymentKeyRepository paymentKeyRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final StudentPaymentHistoryRepository studentPaymentHistoryRepository;

    @DeleteMapping("/deletePaymentHistory/{yearMonth}")
    public ResponseEntity<String> deletePaymentData(@PathVariable(name = "yearMonth") YearMonth yearMonth) {
        String userId = SecurityUtils.getCurrentUserId();
        // 1.삭제할 studentId 찾기
        List<Long> studentIds = studentPaymentHistoryRepository.findStudentIdsByUserIdAndYearMonth(userId, yearMonth);
        // 2.PaymentHistory, StudentPaymentHistory 객체 삭제
        long deletedPaymentHistoryCount = paymentHistoryRepository.deleteByUserIdAndYearMonth(userId, yearMonth);
        // 3.PaymentKey 객체 삭제
        long deletedPaymentKeys = paymentKeyRepository.deleteByStudentIds(studentIds);

        return ResponseEntity.ok("Deleted excel data: " + deletedPaymentHistoryCount + "\nDeleted payment keys: " + deletedPaymentKeys);
    }
}
