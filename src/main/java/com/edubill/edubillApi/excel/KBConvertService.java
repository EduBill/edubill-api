package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.payment.domain.PaymentHistory;


import com.edubill.edubillApi.payment.service.PaymentListCreationService;
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


@Service
@RequiredArgsConstructor
@Slf4j
public class KBConvertService implements ConvertService {

    private final PaymentListCreationService paymentListCreationService;
    private static final String BANK_NAME = "국민은행";

    @Transactional
    @Override
    public List<PaymentHistory> convertBankExcelDataToPaymentInfo(MultipartFile file, String userId) throws IOException {

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        Workbook workbook = null;

        if (fileExtension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        } else if (fileExtension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else {
            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다. xls 및 xlsx 파일만 지원됩니다.");
        }

        List<PaymentHistory> paymentHistories = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(0);

        for (int rowNumber = 6; rowNumber < sheet.getPhysicalNumberOfRows() - 1; rowNumber++) {

            DataFormatter formatter = new DataFormatter();
            Row row = sheet.getRow(rowNumber);

            // 거래날짜
            String originalDateTime = formatter.formatCellValue(row.getCell(0));
            LocalDateTime depositDateTime = LocalDateTime.parse(originalDateTime, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
            LocalDate depositDate = depositDateTime.toLocalDate();


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

            paymentListCreationService.createPaymentHistoryList(depositDate, depositorName, BANK_NAME, depositAmount, memo, paymentHistories, userId);
        }
        return paymentHistories;
    }
}
