package com.edubill.edubillApi.service.convert;

import com.edubill.edubillApi.Validator;
import com.edubill.edubillApi.domain.enums.PaymentType;
import com.edubill.edubillApi.dto.payment.PaymentHistoryDto;
import com.edubill.edubillApi.domain.PaymentHistory;


import com.edubill.edubillApi.error.exception.ParseNotFoundException;
import com.edubill.edubillApi.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service("KOOKMINconvertService")
public class KOOKMINConvertService implements ConvertService {
    private static final String BANK_NAME = "KOOKMIN";
    private final Validator validator;

    private LocalDateTime parseDateTime(String originalDateTime){
        List<String> dateTime = new ArrayList<>(Arrays.asList(
                "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm",
                "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
                "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:",
                "M/dd/yy HH:mm:ss", "M/dd/yy HH:mm",
                "M/d/yy HH:mm:ss", "M/d/yy HH:mm",
                "MM/dd/yy HH:mm:ss", "MM/dd/yy HH:mm"));
        LocalDateTime depositDateTime = null;

        for (int i=0; i<dateTime.size();i++){
            try {
                depositDateTime = LocalDateTime.parse(originalDateTime, DateTimeFormatter.ofPattern(dateTime.get(i)));
            }
            catch (Exception e){
                continue;
            }
        }

        if (depositDateTime.equals(null)){
            throw new ParseNotFoundException("날짜를 파싱할 수 없습니다. 입력 날짜 형식: "+originalDateTime);
        }
        return depositDateTime;

    }
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

        for (int rowNumber = 5; rowNumber < sheet.getPhysicalNumberOfRows() - 1; rowNumber++) {

            DataFormatter formatter = new DataFormatter();
            Row row = sheet.getRow(rowNumber);

            // 거래날짜
            String originalDateTime = formatter.formatCellValue(row.getCell(0));
            LocalDateTime depositDateTime = parseDateTime(originalDateTime);

            // 입금액
            Cell depositAmountCell = row.getCell(5);
            int depositAmount = (int) depositAmountCell.getNumericCellValue();

            // 보낸분(입금자)
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
            // 메모
            String memo = formatter.formatCellValue(row.getCell(3));

            PaymentHistoryDto paymentHistoryDto = new PaymentHistoryDto(depositDateTime, depositorName, BANK_NAME, depositAmount, memo);

            PaymentHistory paymentHistory = PaymentHistory.toEntity(paymentHistoryDto, PaymentType.BANK_TRANSFER, userId);
            paymentHistories.add(paymentHistory);
        }
        return paymentHistories;
    }
}
