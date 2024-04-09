package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.domain.BankName;
import com.edubill.edubillApi.excel.ExcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.apache.commons.io.FilenameUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/excel")
public class ExcelController {

    private final ExcelService excelService;

    @Operation(summary = "엑셀 데이터 변환",
            description = "각 은행별 엑셀을 받아 하나의 Entity에 매핑하여 동일한 DB에 저장될 수 있도록 한다.",
            parameters = {
                    @Parameter(name = "file",
                            description = "엑셀파일을 입력받는다.",
                            required = true,
                            example = "은행거래내역.xls",
                            schema = @Schema(type = "String",format = "binary"),
                            content = @Content(mediaType = "multipart/form-data")),
                    @Parameter(name="bankCode",
                            description = "은행코드를 입력받는다.",
                            required = true,
                            example = "004",
                            schema = @Schema(type = "String", example = "004"),
                            content = @Content(examples = @ExampleObject(name = "은행코드", value = "004"))
                    )
            })
    @PostMapping("/upload")
    public ResponseEntity<String> readExcel(@RequestParam("file") MultipartFile file, @RequestParam("bankCode") String bankCode, Principal principal) throws IOException {

        final String userId = principal.getName();
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());

        if (fileExtension.equals("xls") || fileExtension.equals("xlsx")) {
            String bankName = BankName.getBankNameByCode(bankCode);
            excelService.convertExcelDataByBankCode(file, bankName, userId);
        } else {
            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다. xls 및 xlsx 파일만 지원됩니다.");
        }
        return ResponseEntity.ok("excel upload 완료");
    }
}