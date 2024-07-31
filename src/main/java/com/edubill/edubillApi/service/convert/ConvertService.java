package com.edubill.edubillApi.service.convert;

import com.edubill.edubillApi.domain.PaymentHistory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ConvertService {
    List<PaymentHistory> convertBankExcelDataToPaymentHistory(MultipartFile file, String UserId) throws IOException;
}
