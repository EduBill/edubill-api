package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.dto.ExcelResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.YearMonth;

public interface ExcelService {

    void convertExcelDataByBankCodeAndGeneratePaymentKey(MultipartFile file, String bankName, final String userId, YearMonth yearMonth) throws IOException;

    void changeExcelUploadedStatusByYearMonthAndUserId(YearMonth yearMonth, final String userId);

    Boolean getExcelUploadStatus(String userId, YearMonth yearMonth);
}
