package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.dto.student.StudentInfoTestRequestDto;
import com.edubill.edubillApi.error.exception.UserNotFoundException;
import com.edubill.edubillApi.repository.ExcelUploadStatusRepository;
import com.edubill.edubillApi.repository.StudentPaymentHistoryRepository;
import com.edubill.edubillApi.repository.payment.PaymentHistoryRepository;
import com.edubill.edubillApi.repository.payment.PaymentKeyCustomRepository;
import com.edubill.edubillApi.repository.payment.PaymentKeyRepository;
import com.edubill.edubillApi.repository.users.UserRepository;
import com.edubill.edubillApi.service.StudentService;
import com.edubill.edubillApi.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    private final ExcelUploadStatusRepository excelUploadStatusRepository;
    private final UserRepository userRepository;
    private final StudentService studentService;

    @Operation(summary = "학생/반 테스트 데이터 추가",
            description = "임의의 학생과 반 정보를 등록한다.")
    @PostMapping("/addStudent")
    public ResponseEntity<String> addStudentAndGroupInfoTest(@Validated @RequestBody StudentInfoTestRequestDto studentInfoTestRequestDto, final Principal principal) {
        final String userId = principal.getName();
        studentService.addStudentAndGroupInfoTest(studentInfoTestRequestDto, userId);
        return ResponseEntity.ok("학생 테스트 데이터 추가 완료");
    }
}
