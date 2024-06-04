package com.edubill.edubillApi.controller;


import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.dto.StudentInfoRequestDto;
import com.edubill.edubillApi.dto.user.UserProfileDto;
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

    @Operation(summary = "학생 Mock데이터 생성",
            description = "학정 정보를 등록한다.")
    @PostMapping("/test/generateStudentInfo")
    public ResponseEntity<String> addStudentInfo(
            @Validated @RequestBody  StudentInfoRequestDto studentInfoRequestDto,
            final Principal principal) {

        final String userId = principal.getName();
        studentService.addStudentInfo(studentInfoRequestDto, userId);

        return ResponseEntity.ok("학생 추가 완료");
    }
}
