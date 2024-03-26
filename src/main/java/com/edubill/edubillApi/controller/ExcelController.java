package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.service.ConvertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/excel")
public class ExcelController {

    private final ConvertService convertService;

    @PostMapping("/upload")
    public String readExcel(@RequestParam("file") MultipartFile file) throws IOException {

        /**
         * 어떤 은행인지에 따라 service이동하도록 조건문 추가필요
         */

        convertService.convertExcelFile(file);

        return "ok";
    }
}