package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.domain.PaymentInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class WOORIConvertService implements ConvertService {
    @Override
    public List<PaymentInfo> convertBankExcelDataToPaymentInfo(MultipartFile file){

        return null;
    }
}
