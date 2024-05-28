package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.domain.ExcelUploadStatus;
import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.error.exception.UserNotFoundException;
import com.edubill.edubillApi.repository.ExcelUploadStatusRepository;
import com.edubill.edubillApi.repository.users.UserRepositoryInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.YearMonth;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@Qualifier("excelServiceMock")
@RequiredArgsConstructor
public class ExcelServiceMock implements ExcelService{

    private final ExcelUploadStatusRepository excelUploadStatusRepository;
    private final UserRepositoryInterface userRepositoryInterface;

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

    @Override
    @Transactional
    public void changeExcelUploadedStatusByYearMonthAndUserId(YearMonth yearMonth, String userId) {
        String yearMonthString = yearMonth.toString();
        User user = userRepositoryInterface.findById(userId).orElseThrow(
                ()->new UserNotFoundException("존재하지 않는 유저입니다.  userId: "+ userId));
        ExcelUploadStatus excelUploadStatus = excelUploadStatusRepository.findByYearMonthAndUser(yearMonthString, user).orElse(null);


        // 특정 유저와 특정 연월에 대한 엑셀업로드 여부가 없을 경우 새로 생성
        if (excelUploadStatus == null) {
            excelUploadStatusRepository.save(ExcelUploadStatus.builder()
                    .user(user)
                    .yearMonth(yearMonthString)
                    .isExcelUploaded(true)
                    .build());
        }else{
            excelUploadStatusRepository.save(excelUploadStatus.toBuilder()
                    .isExcelUploaded(false)
                    .build());
        }
    }

    @Override
    @Transactional
    public Boolean getExcelUploadStatus(String userId, YearMonth yearMonth) {
        String yearMonthString = yearMonth.toString();

        User user = userRepositoryInterface.findById(userId).orElseThrow(
                ()->new UserNotFoundException("존재하지 않는 유저입니다.  userId: "+ userId));
        ExcelUploadStatus excelUploadStatus = excelUploadStatusRepository.findByYearMonthAndUser(yearMonthString, user).orElse(null);

        // 특정 유저와 특정 연월에 대한 엑셀업로드 여부가 없을 경우 새로 생성
        if (excelUploadStatus == null) {
            excelUploadStatus = excelUploadStatusRepository.save(ExcelUploadStatus.builder()
                    .user(user)
                    .yearMonth(yearMonthString)
                    .isExcelUploaded(false) //default는 업로드 안 한 상태
                    .build());
        }
        return excelUploadStatus.getIsExcelUploaded();
    }
}
