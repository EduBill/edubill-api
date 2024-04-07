package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.payment.domain.PaymentHistory;
import com.edubill.edubillApi.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final KBConvertService kbConvertService;
    private final SHINHANConvertService shinhanConvertService;
    private final WOORIConvertService wooriConvertService;
    private final HANAConvertService hanaConvertService;
    private final PaymentService paymentService;


    public void convertExcelDataByBankCode(MultipartFile file, String bankCode, final String userId) throws IOException {
        List<PaymentHistory> paymentHistories;

        switch (bankCode) {
            case "004":
                paymentHistories = kbConvertService.convertBankExcelDataToPaymentInfo(file, userId);
                break;
            case "088":
                paymentHistories = shinhanConvertService.convertBankExcelDataToPaymentInfo(file, userId);
                break;
            case "020":
                paymentHistories = wooriConvertService.convertBankExcelDataToPaymentInfo(file, userId);
                break;
            case "081":
                paymentHistories = hanaConvertService.convertBankExcelDataToPaymentInfo(file, userId);
                break;

            default:
                throw new IllegalArgumentException("Unsupported bank code");
        }
        paymentService.savePaymentHistories(paymentHistories);
    }
}
