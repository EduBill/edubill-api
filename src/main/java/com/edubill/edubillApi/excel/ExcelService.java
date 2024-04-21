package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.domain.BankName;
import com.edubill.edubillApi.payment.domain.PaymentHistory;
import com.edubill.edubillApi.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final PaymentService paymentService;
    private final ConvertServiceResolver convertServiceResolver;

    public void convertExcelDataByBankCode(MultipartFile file, String bankName, final String userId) throws IOException {

        ConvertService convertService = convertServiceResolver.resolve(BankName.valueOf(bankName));
        List<PaymentHistory> paymentHistories = convertService.convertBankExcelDataToPaymentHistory(file, userId);

        paymentService.savePaymentHistories(paymentHistories);
    }
}
