package com.edubill.edubillApi.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ConvertService {
    void convertExcelFile(MultipartFile file) throws IOException;
}
