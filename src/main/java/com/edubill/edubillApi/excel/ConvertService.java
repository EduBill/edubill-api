package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.payment.domain.PaymentHistory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ConvertService {
    List<PaymentHistory> convertBankExcelDataToPaymentHistory(MultipartFile file, String UserId) throws IOException;
}
