package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.domain.PaymentInfo;

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
    public List<PaymentInfo> convertBankExcelDataToPaymentInfo(MultipartFile file) throws IOException {

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        Workbook workbook = null;

        if (fileExtension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        } else if (fileExtension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        }

        List<PaymentInfo> paymentInfos = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(0);

        for (int rowNumber = 6; rowNumber < sheet.getPhysicalNumberOfRows() - 1; rowNumber++) {

            DataFormatter formatter = new DataFormatter();
            Row row = sheet.getRow(rowNumber);

            // 거래시간 (format 통일 : yyyyMMddHHmmss)
            String originalDateTime = formatter.formatCellValue(row.getCell(0));
            LocalDateTime dateTime = LocalDateTime.parse(originalDateTime, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
            String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            // 입금액
            Cell depositAmountCell = row.getCell(5);
            int depositAmount = (int) depositAmountCell.getNumericCellValue();

            // 보낸분(입금자)
            String depositorName = "";
            if (depositAmount > 0) {
                // 입금액이 양수인 경우에만 처리
                depositorName = formatter.formatCellValue(row.getCell(2));
            }

            // 메모
            String memo = formatter.formatCellValue(row.getCell(3));

            paymentListCreationService.createPaymentInfoList(formattedDateTime, depositorName, BANK_NAME, depositAmount, memo, paymentInfos);
        }
        return paymentInfos;
    }
}
