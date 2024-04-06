package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.payment.response.PaymentHistoryResponse;
import com.edubill.edubillApi.payment.response.PaymentStatusDto;
import com.edubill.edubillApi.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.YearMonth;

@RestController
@RequestMapping("/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/status/{yearMonth}")

    @Operation(summary = "납부 현황 가져오기",
            description = "주어진 연도와 달에 해당하는 납부 상태를 가져온다.",
            parameters = {
                    @Parameter(name = "yearMonth",
                            description = "납부 상태를 가져오고 싶은 연도와 달. 포멧은 다음과 같다. YYYY-MM",
                            required = true,
                            example = "2023-04",
                            schema = @Schema(type = "string", pattern = "^\\d{4}-\\d{2}$", example = "2023-04"),
                            content = @Content(examples = @ExampleObject(name = "yearMonthExample", value = "2023-04")))
            })
    public ResponseEntity<PaymentStatusDto> getPaymentStatus(@PathVariable(name = "yearMonth") YearMonth yearMonth, Principal principal) {
        final String userId = principal.getName();

        return ResponseEntity.ok(paymentService.getPaymentStatusForManagerInMonth(userId, yearMonth));
    }

    @GetMapping("/paidHistories/{yearMonth}")
    @Operation(summary = "납부 이력 가져오기",
            description = "주어진 연도와 달에 해당하는 납부 이력의 페이지를 가져온다.",
            parameters = {
                    @Parameter(name = "yearMonth",
                            description = "납부 이력을 조회하고 싶은 연도와 달. 포맷은 다음과 같다: YYYY-MM",
                            required = true,
                            example = "2023-04",
                            schema = @Schema(type = "string", pattern = "^\\d{4}-\\d{2}$", example = "2023-04")),
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
    public ResponseEntity<Page<PaymentHistoryResponse>> getPaidHistories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable(name = "yearMonth") YearMonth yearMonth,
            Principal principal) {
        final String userId = principal.getName();

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(paymentService.getPaidHistoriesForManagerInMonth(userId, yearMonth, pageable));
    }
}
