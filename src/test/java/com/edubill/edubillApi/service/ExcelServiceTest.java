package com.edubill.edubillApi.service;

import com.edubill.edubillApi.config.TestcontainerConfig;
import com.edubill.edubillApi.domain.PaymentHistory;
import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.service.convert.HANAConvertService;
import com.edubill.edubillApi.service.convert.SHINHANConvertService;
import com.edubill.edubillApi.service.excel.ExcelServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@TestcontainerConfig
public class ExcelServiceTest {

    @Autowired
    ExcelServiceImpl excelService;
    @Autowired
    HANAConvertService hanaConvertService;
    @Autowired
    SHINHANConvertService shinhanConvertService;

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }

    @Test
    @DisplayName("하나은행 엑셀 데이터 업로드")
    void convertBankDataToPaymentHistory_Hana() throws IOException {

        //given
        User user = User.builder()
                .userId("1")
                .build();
        String fileName = "testHanaUpload";
        String contentType = "xls";
        String filePath = "src/test/resources/testHanaUpload.xls";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        //when
        List<PaymentHistory> paymentHistories = hanaConvertService.convertBankExcelDataToPaymentHistory(mockMultipartFile, user.getUserId());

        //then
        assertThat(paymentHistories.size()).isEqualTo(8);
        assertThat(paymentHistories.get(0).getBankName()).isEqualTo("HANA");
        assertThat(paymentHistories.get(0).getDepositorName()).isEqualTo("홍길동 (테스트테스트용)");
        assertThat(paymentHistories.get(0).getPaidAmount()).isEqualTo(200000);
    }

    @Test
    @DisplayName("신한은행 엑셀 데이터 업로드")
    void convertBankDataToPaymentHistory_ShinHan() throws IOException {

        //given
        User user = User.builder()
                .userId("1")
                .build();
        String fileName = "testShinHanUpload";
        String contentType = "xls";
        String filePath = "src/test/resources/testShinHanUpload.xls";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        //when
        List<PaymentHistory> paymentHistories = shinhanConvertService.convertBankExcelDataToPaymentHistory(mockMultipartFile, user.getUserId());

        //then
        assertThat(paymentHistories.size()).isEqualTo(7);
        assertThat(paymentHistories.get(0).getBankName()).isEqualTo("SHINHAN");
        assertThat(paymentHistories.get(0).getDepositorName()).isEqualTo("학부모1");
        assertThat(paymentHistories.get(0).getPaidAmount()).isEqualTo(200000);
    }
}
