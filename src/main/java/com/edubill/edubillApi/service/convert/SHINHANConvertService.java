package com.edubill.edubillApi.service.convert;

import com.edubill.edubillApi.Validator;
import com.edubill.edubillApi.domain.enums.PaymentType;
import com.edubill.edubillApi.dto.payment.PaymentHistoryDto;
import com.edubill.edubillApi.domain.PaymentHistory;

import com.edubill.edubillApi.service.PaymentService;
import com.edubill.edubillApi.service.convert.ConvertService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service("SHINHANconvertService")
public class SHINHANConvertService implements ConvertService {

    private final PaymentService paymentService;
    private static final String BANK_NAME = "SHINHAN";
    private final Validator validator;

    @Override
    public List<PaymentHistory> convertBankExcelDataToPaymentHistory(MultipartFile file, String userId) throws IOException {

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

        for (int rowNumber = 7; rowNumber < sheet.getPhysicalNumberOfRows(); rowNumber++) {

            DataFormatter formatter = new DataFormatter();
            Row row = sheet.getRow(rowNumber);

            // 거래날짜
            String originalDate = formatter.formatCellValue(row.getCell(0));
            LocalDate depositDate = LocalDate.parse(originalDate);

            // 거래시간
            String originalTime = formatter.formatCellValue(row.getCell(1));
            LocalTime depositTime = LocalTime.parse(originalTime);

            // LocalDateTime으로 합치기
            LocalDateTime depositDateTime = depositDate.atTime(depositTime);

            // 입금액
            Cell depositAmountCell = row.getCell(4);
            int depositAmount = (int) depositAmountCell.getNumericCellValue();

            // 보낸분(입금자)
            String depositorName = "";
            if (depositAmount > 0) {
                // 입금액이 양수인 경우에만 처리
                depositorName = formatter.formatCellValue(row.getCell(5));
                // 한글 이외의 영어, 숫자, 문자가 포함된 경우 해당 행의 데이터를 전달하지 않음
                // 영어가,포함된 경우 해당 행의 데이터를 전달하지 않음
                if (!validator.isValidDepositorName(depositorName)) {
                    continue;
                }
            } else {
                continue;
            }

            // TODO: 신한은행의 경우 메모가 존재하지 않아 제거 필요
            String memo = "";

            PaymentHistoryDto paymentHistoryDto = new PaymentHistoryDto(depositDateTime, depositorName, BANK_NAME, depositAmount, memo);

            PaymentHistory paymentHistory = PaymentHistory.toEntity(paymentHistoryDto, PaymentType.BANK_TRANSFER, userId);

            paymentHistories.add(paymentHistory);
        }
        return paymentHistories;
    }
}
