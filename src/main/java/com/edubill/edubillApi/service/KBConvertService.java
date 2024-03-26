package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.PaymentInfo;

import com.edubill.edubillApi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KBConvertService implements ConvertService {

    private final PaymentRepository paymentRepository;

    @Override
    public void convertExcelFile(MultipartFile file) throws IOException {

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        Workbook workbook = null;

        if (fileExtension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        } else if (fileExtension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        }

        /**
         * 2024.04.02
         * 은행 별 엑셀 데이터에 대한 정보가 필요하여 수정이 필요
         */
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {

            DataFormatter formatter = new DataFormatter();
            Row row = sheet.getRow(i);

            Cell localDateTimeCell = row.getCell(0);
            LocalDateTime transactionDateTime = localDateTimeCell.getLocalDateTimeCellValue();


            String studentName = formatter.formatCellValue(row.getCell(1));
            String bankName = formatter.formatCellValue(row.getCell(2));

            Cell depositAmountCell = row.getCell(3);
            int depositAmount = (int) depositAmountCell.getNumericCellValue();

            String memo = formatter.formatCellValue(row.getCell(4));

            //Entity에 mapping
            PaymentInfo paymentInfo = new PaymentInfo(transactionDateTime, studentName, bankName, depositAmount, memo);

            // db에 저장
            paymentRepository.save(paymentInfo);
        }
    }
}
