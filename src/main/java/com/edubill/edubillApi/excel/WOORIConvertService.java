package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.payment.domain.PaymentHistory;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service("WOORIconvertService")
public class WOORIConvertService implements ConvertService {
    @Override
    public List<PaymentHistory> convertBankExcelDataToPaymentInfo(MultipartFile file, String userId){

        return null;
    }
}
