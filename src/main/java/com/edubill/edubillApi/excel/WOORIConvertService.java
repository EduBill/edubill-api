package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.domain.PaymentHistory;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service("WOORIconvertService")
public class WOORIConvertService implements ConvertService {
    @Override
    public List<PaymentHistory> convertBankExcelDataToPaymentHistory(MultipartFile file, String userId){

        return null;
    }
}
