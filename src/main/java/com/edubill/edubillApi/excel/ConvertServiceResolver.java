package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.domain.BankName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ConvertServiceResolver {

    @Qualifier("KOOKMINconvertService")
    @Autowired
    private ConvertService kookminConvertService;

    @Qualifier("SHINHANconvertService")
    @Autowired
    private ConvertService shinhanConvertService;

    @Qualifier("HANAconvertService")
    @Autowired
    private ConvertService hanaConvertService;

    @Qualifier("WOORIconvertService")
    @Autowired
    private ConvertService wooriConvertService;

    public ConvertService resolve(BankName bankName) {

        ConvertService convertService;
        switch (bankName) {
            case KOOKMIN:
                convertService = kookminConvertService;
                break;
            case SHINHAN:
                convertService = shinhanConvertService;
                break;
            case HANA:
                convertService = hanaConvertService;
                break;
            case WOORI:
                convertService = wooriConvertService;
                break;
            default:
                convertService = null;
        }
        return convertService;
    }
}
