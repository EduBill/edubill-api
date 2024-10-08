package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.dto.FileUrlResponseDto;
import com.edubill.edubillApi.dto.payment.*;
import com.edubill.edubillApi.error.exception.UserNotFoundException;
import com.edubill.edubillApi.service.excel.ExcelService;
import com.edubill.edubillApi.service.PaymentService;
import com.edubill.edubillApi.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.time.YearMonth;
import java.util.List;

@Tag(name = "Payment", description = "결제내역관리 API")
@RestController
@RequestMapping("/v1/payment")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final ExcelService excelService;


    public PaymentController(PaymentService paymentService, @Qualifier("excelServiceImpl") ExcelService excelService) {
        this.paymentService = paymentService;
        this.excelService = excelService;
    }

    @GetMapping("/status/{yearMonth}")

    @Operation(summary = "납부 현황 가져오기",
            description = "주어진 연도와 달에 해당하는 납부 상태를 가져온다.",
            parameters = {
                    @Parameter(name = "yearMonth",
                            description = "납부 상태를 가져오고 싶은 연도와 달. 포멧은 다음과 같다. YYYY-MM",
                            required = true,
                            example = "2024-04",
                            schema = @Schema(type = "string", pattern = "^\\d{4}-\\d{2}$", example = "2024-04"),
                            content = @Content(examples = @ExampleObject(name = "yearMonthExample", value = "2024-04")))
            })
    public ResponseEntity<PaymentStatusDto> getPaymentStatus(@PathVariable(name = "yearMonth") YearMonth yearMonth, Principal principal) {
        final String userId = principal.getName();

        PaymentStatusDto paymentStatusDto = paymentService.getPaymentStatusForManagerInMonth(userId, yearMonth);
        Boolean isExcelUploaded = excelService.getExcelUploadStatus(userId, yearMonth);

        log.info("isExcelUploaded = {}", isExcelUploaded);

        PaymentStatusDto updatedPaymentStatusDto = paymentStatusDto.toBuilder()
                .isExcelUploaded(isExcelUploaded)
                .build();

        return new ResponseEntity<>(updatedPaymentStatusDto, HttpStatus.OK);
    }

    @GetMapping("/paidHistories/{yearMonth}")
    @Operation(summary = "납부 이력 가져오기",
            description = "주어진 연도와 달에 해당하는 납부 이력의 페이지를 가져온다.",
            parameters = {
                    @Parameter(name = "yearMonth",
                            description = "납부 이력을 조회하고 싶은 연도와 달. 포맷은 다음과 같다: YYYY-MM",
                            required = true,
                            example = "2023-04",
                            schema = @Schema(type = "string", pattern = "^\\d{4}-\\d{2}$", example = "2024-04")),
                    @Parameter(name = "page",
                            description = "요청 페이지 번호 (0부터 시작)",
                            required = false,
                            example = "0",
                            schema = @Schema(type = "integer", defaultValue = "0")),
                    @Parameter(name = "size",
                            description = "페이지당 데이터 수",
                            required = false,
                            example = "10",
                            schema = @Schema(type = "integer", defaultValue = "10"))
            })
    public ResponseEntity<Page<PaymentHistoryResponseDto>> getPaidHistories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable(name = "yearMonth") YearMonth yearMonth,
            Principal principal) {
        final String userId = principal.getName();

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(paymentService.getPaidHistoriesForManagerInMonth(userId, yearMonth, pageable));
    }

    @GetMapping("/unpaidHistories/{yearMonth}")
    @Operation(summary = "미납부 이력 가져오기",
            description = "주어진 연도와 달에 해당하는 미납부페이지를 가져온다.",
            parameters = {
                    @Parameter(name = "yearMonth",
                            description = "미납부 이력을 조회하고 싶은 연도와 달. 포맷은 다음과 같다: YYYY-MM",
                            required = true,
                            example = "2023-04",
                            schema = @Schema(type = "string", pattern = "^\\d{4}-\\d{2}$", example = "2024-04")),
                    @Parameter(name = "page",
                            description = "요청 페이지 번호 (0부터 시작)",
                            required = false,
                            example = "0",
                            schema = @Schema(type = "integer", defaultValue = "0")),
                    @Parameter(name = "size",
                            description = "페이지당 데이터 수",
                            required = false,
                            example = "10",
                            schema = @Schema(type = "integer", defaultValue = "10"))
            })
    public ResponseEntity<Page<PaymentHistoryResponseDto>> getUnpaidHistories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable(name = "yearMonth") YearMonth yearMonth,
            Principal principal) {
        final String userId = principal.getName();

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(paymentService.getUnpaidHistoriesForManagerInMonth(userId, yearMonth, pageable));
    }

    @Operation(summary = "납부 상세 내역 가져오기",
            description = "특정 납부 내역의 상세 내역을 가져온다.")
    @GetMapping("/paidHistoryDetails/{paymentHistoryId}")
    public ResponseEntity<PaymentHistoryDetailResponse> getPaymentHistoryDetail(
            @Parameter(description = "특정 납부내역의 아이디", example = "1", required = true)
            @PathVariable long paymentHistoryId) {
        PaymentHistoryDetailResponse paymentHistoryDetailResponse= paymentService.findPaymentHistoryById(paymentHistoryId);
        return ResponseEntity.ok(paymentHistoryDetailResponse);
    }

    @Operation(summary = "결제 키 생성 및 저장",
            description = "결제 확인된 내역에 대해 결제 키를 생성하고 납부상태를 완료로 체크한다.")
    @PostMapping("/generateKeys/{yearMonth}")
    public ResponseEntity<?> generatePaymentKeys(@PathVariable(name = "yearMonth") YearMonth yearMonth, Principal principal) {
        final String userId = principal.getName();
        paymentService.handleStudentPaymentProcessing(yearMonth, userId);

        return ResponseEntity.ok("학생 납부처리 후 결제키 생성 완료");
    }

    @Operation(summary = "메모업데이트하기",
            description = "납부 상세내역에서 기존 메모를 업데이트한다.")
    @PutMapping("/memo")
    public ResponseEntity<MemoResponseDto> updateMemoDescription(@RequestBody MemoRequestDto memoRequestDto) {
        MemoResponseDto memoResponseDto = paymentService.updateMemo(memoRequestDto);
        return ResponseEntity.ok(memoResponseDto);
    }

    @Operation(summary = "미납 내역 수동처리",
            description = "미납 리스트에 있는 학생과 미확인 입금 내역을 연결하여 결제키를 생성한 후 수동으로 완납 처리 한다.")
    @PostMapping("/manualProcessing")
    public ResponseEntity<HttpStatus> manualProcessingOfUnpaidHistory(@RequestBody UnpaidHistoryRequestDto unpaidHistoryRequestDto ){
       Long studentId = unpaidHistoryRequestDto.getStudentId();
       Long paymentHistoryId = unpaidHistoryRequestDto.getPaymentHistoryId();
       YearMonth yearMonth = unpaidHistoryRequestDto.getYearMonth();

       paymentService.manualProcessingOfUnpaidHistory(studentId, paymentHistoryId, yearMonth);
       return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "미납 내역 수동처리 - 납부내역 직접 입력",
            description = "수동으로 완납처리 시 납부내역을 직접 입력하여 연결한다.")
    @PutMapping("/manualProcessing/input")
    public ResponseEntity<FileUrlResponseDto> manualProcessingOfUnpaidHistoryByManualInput(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수동 납부내역 입력 요청", content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = ManualPaymentHistoryRequestDto.class)))
            @ModelAttribute ManualPaymentHistoryRequestDto manualPaymentHistoryRequestDto) throws IOException {

        FileUrlResponseDto fileUrlResponseDto = paymentService.manualProcessingOfUnpaidHistoryByManualInput(manualPaymentHistoryRequestDto);
        return ResponseEntity.ok(fileUrlResponseDto);
    }

    @Operation(summary = "미납 학생 조회하기",
            description = "조회하는 월에 미납한 학생들을 조회한다.")
    @GetMapping("/unpaidStudents/{yearMonth}")
    public ResponseEntity<List<UnpaidStudentsResponseDto>> unPaidStudents(@PathVariable(name = "yearMonth") YearMonth yearMonth){
        String userId = SecurityUtils.getCurrentUserId();

        return ResponseEntity.ok(paymentService.getUnpaidStudentsList(userId, yearMonth));
    }
    @DeleteMapping("/deletePaymentHistory/{yearMonth}")
    @Transactional
    public ResponseEntity<String> deletePaymentData(@PathVariable(name = "yearMonth") YearMonth yearMonth) {
        String userId = SecurityUtils.getCurrentUserId();
        long deletedPaymentHistoryCount = paymentService.deleteExcelData(userId, yearMonth);

        return ResponseEntity.ok("Deleted excel data: " + deletedPaymentHistoryCount );
    }
}
