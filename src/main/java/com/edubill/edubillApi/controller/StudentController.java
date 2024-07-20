package com.edubill.edubillApi.controller;


import com.edubill.edubillApi.dto.student.StudentInfoRequestDto;
import com.edubill.edubillApi.dto.student.StudentInfoTestRequestDto;
import com.edubill.edubillApi.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping
    public ResponseEntity<?> addStudentInfo(@RequestBody StudentInfoRequestDto studentInfoRequestDto) {

        return ResponseEntity.ok("ok");
    }

    @Operation(summary = "학생 Mock데이터 생성",
            description = "학정 정보를 등록한다.")
    @PostMapping("/test/generateStudentInfo")
    public ResponseEntity<String> addStudentInfoTest(
            @Validated @RequestBody StudentInfoTestRequestDto studentInfoTestRequestDto,
            final Principal principal) {

        final String userId = principal.getName();
        studentService.addStudentInfo(studentInfoTestRequestDto, userId);

        return ResponseEntity.ok("학생 추가 완료");
    }
}
