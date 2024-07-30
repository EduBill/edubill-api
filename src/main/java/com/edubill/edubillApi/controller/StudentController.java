package com.edubill.edubillApi.controller;


import com.edubill.edubillApi.dto.student.*;
import com.edubill.edubillApi.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.YearMonth;

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
    public ResponseEntity<StudentInfoResponseDto> addStudentInfo(@RequestBody StudentInfoRequestDto studentInfoRequestDto) {
        return ResponseEntity.ok(studentService.addStudentInfo(studentInfoRequestDto));
    }

    @Operation(summary = "새로운 반 추가",
            description = "새로운 반 정보를 추가한다.")
    @PostMapping("/groups")
    public ResponseEntity<GroupInfoResponseDto> addStudentGroupInfo(@RequestBody GroupInfoRequestDto groupInfoRequestDto) {
        return ResponseEntity.ok(studentService.addGroupInfo(groupInfoRequestDto));
    }

    @Operation(summary = "학생 삭제",
            description = "특정 id에 해당하는 학생을 삭제한다.")
    @DeleteMapping("/{studentId}")
    public ResponseEntity<DeletedStudentInfoDto> deleteStudentInfo(@PathVariable(name = "studentId") Long studentId) {
        return ResponseEntity.ok(studentService.deleteStudentInfo(studentId));
    }

    @Operation(summary = "학생/반 테스트 데이터 추가",
            description = "임의의 학생과 반 정보를 등록한다.")
    @PostMapping("/test")
    public ResponseEntity<String> addStudentAndGroupInfoTest(
            @Validated @RequestBody StudentInfoTestRequestDto studentInfoTestRequestDto,
            final Principal principal) {

        final String userId = principal.getName();
        studentService.addStudentAndGroupInfoTest(studentInfoTestRequestDto, userId);

        return ResponseEntity.ok("학생 추가 완료");
    }
}
