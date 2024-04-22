package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.payment.dto.PaymentHistoryDto;
import com.edubill.edubillApi.payment.domain.PaymentHistory;


import com.edubill.edubillApi.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service("KOOKMINconvertService")
public class KOOMINConvertService implements ConvertService {

    private final PaymentService paymentService;
    private static final String BANK_NAME = "국민은행";


    @Override
    public List<PaymentHistory> convertBankExcelDataToPaymentHistory(MultipartFile file, String userId) throws IOException {

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        Workbook workbook = null;
        List<PaymentHistory> paymentHistories = new ArrayList<>();

        if (fileExtension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        } else if (fileExtension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        }
        Sheet sheet = workbook.getSheetAt(0);

        for (int rowNumber = 6; rowNumber < sheet.getPhysicalNumberOfRows() - 1; rowNumber++) {

            DataFormatter formatter = new DataFormatter();
            Row row = sheet.getRow(rowNumber);

            // 거래날짜
            String originalDateTime = formatter.formatCellValue(row.getCell(0));
            LocalDateTime depositDateTime = LocalDateTime.parse(originalDateTime, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));

            // 입금액
            Cell depositAmountCell = row.getCell(5);
            int depositAmount = (int) depositAmountCell.getNumericCellValue();

            // 보낸분(입금자)
            String depositorName = "";
            if (depositAmount > 0) {
                // 입금액이 양수인 경우에만 처리
                depositorName = formatter.formatCellValue(row.getCell(2));
                // 한글 이외의 영어, 숫자, 문자가 포함된 경우 해당 행의 데이터를 전달하지 않음
                if (depositorName.matches(".*[a-zA-Z0-9].*")) {
                    continue;
                }
            } else {
                continue;
            }
            // 메모
            String memo = formatter.formatCellValue(row.getCell(3));

            PaymentHistoryDto paymentHistoryDto = new PaymentHistoryDto(depositDateTime, depositorName, BANK_NAME, depositAmount, memo);

            PaymentHistory paymentHistory = paymentService.mapToPaymentHistoryWithStudentGroup(paymentHistoryDto, userId);

            paymentHistories.add(paymentHistory);
        }
        return paymentHistories;
    }
}