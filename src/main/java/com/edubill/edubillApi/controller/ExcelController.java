package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.excel.ExcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
                            description = "엑셀파일을 입력받는다. 포멧: .xls, .xlsx",
                            required = true,
                            example = "KB거래내역조회.xls",
                            schema = @Schema(type = "MultipartFile",format = "binary"),
                            content = @Content(mediaType = "multipart/form-data")),
                    @Parameter(name="bankCode",
                            description = "bankCode",
                            required = true,
                            example = "004",
                            schema = @Schema(type = "String", example = "004"),
                            content = @Content(examples = @ExampleObject(name = "bankCodeExample", value = "004"))
                    )
            })
    @PostMapping("/upload")
    public ResponseEntity<String> readExcel(@RequestParam("file") MultipartFile file, @RequestParam("bankCode") String bankCode) throws IOException {
        excelService.convertExcelDataByBankCode(file, bankCode);
        return ResponseEntity.ok("excel upload 완료");
    }
}