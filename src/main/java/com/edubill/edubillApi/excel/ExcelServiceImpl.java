package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.domain.BankName;
import com.edubill.edubillApi.domain.PaymentHistory;
import com.edubill.edubillApi.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Qualifier("excelServiceImpl")
@Slf4j
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {

    private final PaymentService paymentService;
    private final ConvertServiceResolver convertServiceResolver;

    //TODO: Async 관련 오류 해결
    //@Async("taskExecutor")
    @Override
    public void convertExcelDataByBankCode(MultipartFile file, String bankName, final String userId) throws IOException {

        ConvertService convertService = convertServiceResolver.resolve(BankName.valueOf(bankName));
        List<PaymentHistory> paymentHistories = convertService.convertBankExcelDataToPaymentHistory(file, userId);

        paymentService.savePaymentHistories(paymentHistories);
    }
}
