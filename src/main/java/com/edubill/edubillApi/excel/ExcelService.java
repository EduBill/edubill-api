package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.payment.domain.PaymentHistory;
import com.edubill.edubillApi.payment.service.PaymentService;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    private static final String SERVICE_NAME_SUFFIX = "convertService";
    private final BeanFactory beanFactory;
    private final PaymentService paymentService;

    @Autowired
    public ExcelService(BeanFactory beanFactory, PaymentService paymentService) {
        this.beanFactory = beanFactory;
        this.paymentService = paymentService;
    }

    public void convertExcelDataByBankCode(MultipartFile file, String bankName, final String userId) throws IOException {

        ConvertService convertService = beanFactory.getBean(getConvertServiceBeanName(bankName), ConvertService.class);
        List<PaymentHistory> paymentHistories = convertService.convertBankExcelDataToPaymentInfo(file, userId);

        paymentService.savePaymentHistories(paymentHistories);
    }

    private String getConvertServiceBeanName(String bankName) {
        return bankName + SERVICE_NAME_SUFFIX;
    }
}
