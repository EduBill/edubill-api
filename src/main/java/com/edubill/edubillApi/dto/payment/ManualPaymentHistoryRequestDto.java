package com.edubill.edubillApi.dto.payment;

import com.edubill.edubillApi.domain.PaymentType;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.YearMonth;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManualPaymentHistoryRequestDto {

    @Schema(description = "학생 ID를 입력한다.", type = "integer", format = "int64", example = "1")
    private Long studentId;

    @Schema(description = "연도-월", type = "String", format = "yearMonth", example = "2024-05")
    private YearMonth yearMonth;

    @Schema(description = "거래유형을 입력한다.", type = "String", example = "현금결제")
    private String paymentTypeString;

    @Schema(description = "거래금액을 입력한다.", type = "integer", format = "int32", example = "3000000")
    private Integer paidAmount;

    @Schema(description = "메모를 입력한다.", type = "String", example = "수동입력처리")
    private String memo;

    @Schema(description = "증빙서류를 업로드한다.", format = "binary")
    private MultipartFile file;
}
