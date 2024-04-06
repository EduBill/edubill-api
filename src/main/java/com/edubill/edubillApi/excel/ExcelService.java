package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.domain.PaymentInfo;
import com.edubill.edubillApi.payment.service.PaymentManagementService;
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
    private final PaymentManagementService paymentManagementService;


    public void convertExcelDataByBankCode(MultipartFile file, String bankCode) throws IOException {
        List<PaymentInfo> paymentInfos;

        switch (bankCode) {
            case "004":
                paymentInfos = kbConvertService.convertBankExcelDataToPaymentInfo(file);
                break;
            case "088":
                paymentInfos = shinhanConvertService.convertBankExcelDataToPaymentInfo(file);
                break;
            case "020":
                paymentInfos = wooriConvertService.convertBankExcelDataToPaymentInfo(file);
            case "081":
                paymentInfos = hanaConvertService.convertBankExcelDataToPaymentInfo(file);

            default:
                throw new IllegalArgumentException("Unsupported bank code");
        }
        paymentManagementService.savePaymentInfos(paymentInfos);
    }
}
