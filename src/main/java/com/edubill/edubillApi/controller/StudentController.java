package com.edubill.edubillApi.controller;


import com.edubill.edubillApi.dto.group.DeletedGroupInfoDto;
import com.edubill.edubillApi.dto.group.GroupInfoRequestDto;
import com.edubill.edubillApi.dto.group.GroupInfoResponseDto;
import com.edubill.edubillApi.dto.group.GroupInfoInAddStudentResponseDto;
import com.edubill.edubillApi.dto.student.*;
import com.edubill.edubillApi.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "반 조회",
            description = "유저가 생성한 모든 반을 가져온다.")
    @GetMapping("/allGroups")
    public ResponseEntity<Page<GroupInfoInAddStudentResponseDto>> findAllGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(studentService.findAllGroupsByUserId(pageable));
    }

    @Operation(summary = "학생 삭제",
            description = "특정 id에 해당하는 학생을 삭제한다.")
    @DeleteMapping("/{studentId}")

    public ResponseEntity<DeletedStudentInfoDto> deleteStudentInfo(@PathVariable(name = "studentId") Long studentId) {
        return ResponseEntity.ok(studentService.deleteStudentInfo(studentId));
    }

    @Operation(summary = "반 삭제",
            description = "특정 id에 해당하는 반을 삭제한다.")
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<DeletedGroupInfoDto> deleteGroupInfo(@PathVariable(name = "groupId") Long groupId) {
        return ResponseEntity.ok(studentService.deleteGroupInfo(groupId));
    }
}
