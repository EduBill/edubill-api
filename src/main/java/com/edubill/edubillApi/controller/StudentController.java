package com.edubill.edubillApi.controller;


import com.edubill.edubillApi.dto.student.StudentInfoRequestDto;
import com.edubill.edubillApi.dto.student.StudentInfoResponseDto;
import com.edubill.edubillApi.dto.student.StudentInfoTestRequestDto;
import com.edubill.edubillApi.service.StudentService;
import com.edubill.edubillApi.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "Student", description = "학생정보관리 API")
@RestController
@RequestMapping("/v1/student")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "새로운 원생 추가",
            description = "새로운 학생정보를 추가한다.")
    @PostMapping
    public ResponseEntity<?> addStudentInfo(@RequestBody StudentInfoRequestDto studentInfoRequestDto) {
        StudentInfoResponseDto studentInfoResponseDto = studentService.addStudentInfo(studentInfoRequestDto);
        return ResponseEntity.ok(studentInfoResponseDto);
    }

    @Operation(summary = "학생 Mock데이터 생성",
            description = "학정 정보를 등록한다.")
    @PostMapping("/test")
    public ResponseEntity<String> addStudentInfoTest(
            @Validated @RequestBody StudentInfoTestRequestDto studentInfoTestRequestDto,
            final Principal principal) {

        final String userId = principal.getName();
        studentService.addStudentInfoTest(studentInfoTestRequestDto, userId);

        return ResponseEntity.ok("학생 추가 완료");
    }
}
