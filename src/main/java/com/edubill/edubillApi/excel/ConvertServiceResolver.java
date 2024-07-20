package com.edubill.edubillApi.excel;

import com.edubill.edubillApi.domain.enums.BankName;
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

        ConvertService convertService = switch (bankName) {
            case KOOKMIN -> kookminConvertService;
            case SHINHAN -> shinhanConvertService;
            case HANA -> hanaConvertService;
            case WOORI -> wooriConvertService;
            default -> throw new IllegalArgumentException("Unsupported bank code");
        };
        return convertService;
    }
}
