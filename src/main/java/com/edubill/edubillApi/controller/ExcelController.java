package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.domain.BankName;
import com.edubill.edubillApi.dto.ExcelResponseDto;
import com.edubill.edubillApi.excel.ExcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;


import org.apache.commons.io.FilenameUtils;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.YearMonth;


@Slf4j
@Tag(name = "Excel", description = "엑셀업로드 API")
@RestController
@RequestMapping("/v1/excel")
public class ExcelController {

    private final ExcelService excelService;

    public ExcelController(@Qualifier("excelServiceImpl") ExcelService excelService) {
        this.excelService = excelService;
    }

    @Operation(summary = "엑셀 데이터 변환",
            description = "각 은행별 엑셀을 받아 하나의 Entity에 매핑하여 동일한 DB에 저장될 수 있도록 한다.")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> readExcel(
            @Parameter(description = "업로드할 엑셀 파일", required = true)
            @RequestPart("file") MultipartFile multipartFile,

            @Parameter(description = "은행 코드", required = true, schema = @Schema(type = "String", example = "004"))
            @RequestPart("bankCode") String bankCode, Principal principal) throws IOException {

        final String userId = principal.getName();
        String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        if (fileExtension.equals("xls") || fileExtension.equals("xlsx")) {
            String bankName = BankName.getBankNameByCode(bankCode);
            excelService.convertExcelDataByBankCode(multipartFile, bankName, userId);
            return ResponseEntity.ok("엑셀 업로드 완료");
        } else {
            throw new IllegalArgumentException("지원되지 않는 파일 형식. xls 및 xlsx 파일만 지원됩니다.");
        }
    }



    @Operation(summary = "엑셀 업로드 상태를 수정",
            description = "엑셀 업로드가 완료된 후 엑셀 업로드 상태를 true로 수정한다.")
    @GetMapping(value = "/status-change/{yearMonth}")
    public ResponseEntity<String> changeExcelUploadedStatus(
            @Parameter(description = "납부연월", required = true, schema = @Schema(type = "YearMonth", example = "2024-04"))
            @PathVariable(name = "yearMonth") YearMonth yearMonth, Principal principal) {
        final String userId = principal.getName();
        excelService.changeExcelUploadedStatusByYearMonthAndUserId(yearMonth, userId);

        return ResponseEntity.ok("changed excelUploadStatus true");
    }
}