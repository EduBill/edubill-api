package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.Academy;
import com.edubill.edubillApi.domain.PaymentInfo;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.error.exception.AcademyNotFoundException;
import com.edubill.edubillApi.error.exception.UserNotFoundException;
import com.edubill.edubillApi.repository.AcademyRepository;
import com.edubill.edubillApi.repository.PaymentRepository;

import com.edubill.edubillApi.user.repository.UserRepository;
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

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final AcademyRepository academyRepository;

    @Transactional(readOnly = true)
    @Override
    public void convertExcelFile(MultipartFile file) throws IOException {

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        Workbook workbook = null;

        if (fileExtension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        } else if (fileExtension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        }

        List<PaymentInfo> paymentInfoList = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 6; i < sheet.getPhysicalNumberOfRows() - 1; i++) {

            DataFormatter formatter = new DataFormatter();
            Row row = sheet.getRow(i);


            // 거래시간 (format 통일 : yyyyMMddHHmmss)
            String originalDateTime = formatter.formatCellValue(row.getCell(0));
            LocalDateTime dateTime = LocalDateTime.parse(originalDateTime, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
            String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            // 은행이름
            String bankName = "국민은행";

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


            createPaymentList(formattedDateTime, depositorName, bankName, depositAmount, memo, paymentInfoList);
        }
        paymentRepository.saveAll(paymentInfoList);
    }

    private void createPaymentList(String formattedDateTime, String depositorName, String bankName, int depositAmount, String memo, List<PaymentInfo> paymentInfoList) {
        //휴대폰번호를 통해 user를 찾고 해당 user가 속한 academy에 대한 정보를 가져와 id를 사용
        String phoneNumber = "01012345678";
        User user = userRepository.findByPhoneNumber(phoneNumber).orElse(null);

        if (user != null) {
            //TODO: userId를 이요해 Academy에 설정값 세팅 필요
            Academy academy = academyRepository.findByUserId(user.getUserId());
            if (academy != null) {
                // PaymentInfo 생성 및 설정
                PaymentInfo paymentInfo = new PaymentInfo(formattedDateTime, depositorName, bankName, depositAmount, memo);
                paymentInfo.setAcademy(academy);
                paymentInfoList.add(paymentInfo);
            } else {
                throw new AcademyNotFoundException("학원이 존재하지 않습니다.");
            }
        } else {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }
    }
}
