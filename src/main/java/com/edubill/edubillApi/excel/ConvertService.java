package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.domain.PaymentInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ConvertService {
    List<PaymentInfo> convertBankExcelDataToPaymentInfo(MultipartFile file) throws IOException;
}
