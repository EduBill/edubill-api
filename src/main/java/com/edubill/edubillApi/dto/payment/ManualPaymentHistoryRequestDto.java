package com.edubill.edubillApi.dto.payment;

import com.edubill.edubillApi.domain.PaymentType;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.YearMonth;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManualPaymentHistoryRequestDto {

    private Long studentId;
    private YearMonth yearMonth;

    private String paymentTypeString; //거래방식
    private Integer paidAmount;
    private String memo;  // 메모

    private MultipartFile file;
}
