package com.edubill.edubillApi.service.convert;

import com.edubill.edubillApi.Validator;
import com.edubill.edubillApi.domain.PaymentHistory;

import com.edubill.edubillApi.domain.enums.PaymentType;
import com.edubill.edubillApi.dto.payment.PaymentHistoryDto;
import com.edubill.edubillApi.service.convert.ConvertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("HANAconvertService")
public class HANAConvertService implements ConvertService {

    private static final String BANK_NAME = "HANA";
    private final Validator validator;

    @Override
    public List<PaymentHistory> convertBankExcelDataToPaymentHistory(MultipartFile file, String userId) throws IOException {

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        Workbook workbook = null;
        List<PaymentHistory> paymentHistories = new ArrayList<>();

        if (fileExtension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        } else if (fileExtension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else {
            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다. xls 및 xlsx 파일만 지원됩니다.");
        }
        Sheet sheet = workbook.getSheetAt(0);

        for (int rowNumber = 6; rowNumber < sheet.getLastRowNum(); rowNumber++) {

            DataFormatter formatter = new DataFormatter();
            Row row = sheet.getRow(rowNumber);

            // 거래날짜
            String originalDateTime = formatter.formatCellValue(row.getCell(0));
            LocalDateTime depositDateTime = LocalDateTime.parse(originalDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // 입금액
            Cell depositAmountCell = row.getCell(4);
            int depositAmount = (int) depositAmountCell.getNumericCellValue();

            // 보낸분(입금자)
            // 하나은행 같은 경우 입금자명 칸이 따로 없이 적요에 기재되어 있음
            String depositorName = "";
            if (depositAmount > 0) {
                // 입금액이 양수인 경우에만 처리
                depositorName = formatter.formatCellValue(row.getCell(2));
                // 영어가,포함된 경우 해당 행의 데이터를 전달하지 않음
                if (!validator.isValidDepositorName(depositorName)) {
                    continue;
                }
            } else {
                continue;
            }
            // 메모 - 하나은행은 메모 존재 하지 않고 추가 메모 시 적요 + 추가메모 형식으로 저장됨
            String memo = "";

            PaymentHistoryDto paymentHistoryDto = new PaymentHistoryDto(depositDateTime, depositorName, BANK_NAME, depositAmount, memo);

            PaymentHistory paymentHistory = PaymentHistory.toEntity(paymentHistoryDto, PaymentType.BANK_TRANSFER, userId);
            paymentHistories.add(paymentHistory);
        }

        return paymentHistories;
    }
}
