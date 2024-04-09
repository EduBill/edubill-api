package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.payment.domain.PaymentHistory;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service("HANAconvertService")
public class HANAConvertService implements ConvertService {
    @Override
    public List<PaymentHistory> convertBankExcelDataToPaymentInfo(MultipartFile file, String userId) throws IOException {

        return null;
    }
}
