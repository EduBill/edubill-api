package com.edubill.edubillApi.excel;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ExcelService {

    void convertExcelDataByBankCode(MultipartFile file, String bankName, final String userId) throws IOException;
}
