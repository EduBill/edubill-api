package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.domain.ExcelPaymentInfo;
import com.edubill.edubillApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/excel")
public class ExcelController {

    private final UserRepository userRepository;

    // 파일 1개만 받도록 설정
    @PostMapping("/upload")
    public String readExcel(@RequestParam("file") MultipartFile file) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());

        // 첫번째 시트 불러오기
        XSSFSheet sheet = workbook.getSheetAt(0);

        for(int i=1;i<sheet.getPhysicalNumberOfRows() ;i++) {

            DataFormatter formatter = new DataFormatter();
            XSSFRow row = sheet.getRow(i);

            String userName = formatter.formatCellValue(row.getCell(0));
            String userEmail = formatter.formatCellValue(row.getCell(1));
            String userId = formatter.formatCellValue(row.getCell(2));
            String userPhone = formatter.formatCellValue(row.getCell(3));
            String userType = formatter.formatCellValue(row.getCell(4));

            //Entity에 data 저장
            ExcelPaymentInfo excelPaymentInfo = new ExcelPaymentInfo(userName,userEmail,userId,userPhone,userType);

            //dto의 데이터를 적절한 db에 저장
            // paidAmount, totalPaymentAmount 등

        }
        return "ok";

    }
}