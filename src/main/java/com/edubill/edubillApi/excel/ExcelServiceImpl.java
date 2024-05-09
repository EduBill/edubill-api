package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.domain.BankName;
import com.edubill.edubillApi.domain.ExcelUploadStatus;
import com.edubill.edubillApi.domain.PaymentHistory;
import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.dto.ExcelResponseDto;
import com.edubill.edubillApi.error.exception.UserNotFoundException;
import com.edubill.edubillApi.repository.ExcelUploadStatusRepository;
import com.edubill.edubillApi.repository.UserRepositoryInterface;
import com.edubill.edubillApi.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.YearMonth;
import java.util.List;

@Service
@Qualifier("excelServiceImpl")
@Slf4j
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {

    private final PaymentService paymentService;
    private final ConvertServiceResolver convertServiceResolver;
    private final ExcelUploadStatusRepository excelUploadStatusRepository;
    private final UserRepositoryInterface userRepositoryInterface;

    //TODO: Async 관련 오류 해결
    //@Async("taskExecutor")
    @Override
    public void convertExcelDataByBankCode(MultipartFile file, String bankName, final String userId) throws IOException {

        ConvertService convertService = convertServiceResolver.resolve(BankName.valueOf(bankName));
        List<PaymentHistory> paymentHistories = convertService.convertBankExcelDataToPaymentHistory(file, userId);

        paymentService.savePaymentHistories(paymentHistories);

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
                    .isExcelUploaded(true)
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
