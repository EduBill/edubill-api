package com.edubill.edubillApi.excel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@Qualifier("excelServiceMock")
public class ExcelServiceMock implements ExcelService{

    private AtomicInteger taskCounter = new AtomicInteger(0); // task 순서를 관리하는 Atomic 변수

    @Override
    @Async("taskExecutor")
    public void convertExcelDataByBankCode(MultipartFile file, String bankName, String userId) throws IOException {
        try {
            Thread.sleep(3500);
            int taskNumber = taskCounter.incrementAndGet(); // task 순서를 증가시키고 해당 번호를 가져옴
            log.info("convertExcelTest = {}", taskNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
