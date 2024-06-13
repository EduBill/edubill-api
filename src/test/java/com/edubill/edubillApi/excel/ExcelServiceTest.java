package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.config.TestcontainerConfig;
import com.edubill.edubillApi.dto.ExcelResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.YearMonth;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestcontainerConfig
class ExcelServiceTest {

    @Autowired
    private @Qualifier("excelServiceMock") ExcelService excelServiceMock;

    private MockMultipartFile file;

    // 테스트 케이스에서 사용할 사용자 ID
    private static final String TEST_USER_ID = "01012345678@phone.auth";

    //@BeforeEach
    public void setUp() throws IOException {
        // 테스트에 필요한 파일을 생성
        File testFile = ResourceUtils.getFile("classpath:KB_거래내역조회.xlsx");
        FileInputStream inputStream = new FileInputStream(testFile);
        this.file = new MockMultipartFile("file", testFile.getName(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", inputStream);

        // SecurityContext에 가짜 사용자 인증 정보 설정
        Authentication authentication = new TestingAuthenticationToken(TEST_USER_ID, null);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
    }

   // @Test
    @DisplayName("엑셀 업로드 비동기 테스트")
    void testConvertExcelDataByBankCodeAsync() throws IOException, InterruptedException {

        for (int i = 0; i < 15; i++) {
            // 비동기 메서드 호출
            String bankName = "KOOKMIN";
            String yearMonthString = "2024-04";
            YearMonth yearMonth = YearMonth.parse(yearMonthString);
            excelServiceMock.convertExcelDataByBankCodeAndGeneratePaymentKey(file, bankName, TEST_USER_ID, yearMonth);
        }
        Thread.sleep(50000);
    }
}