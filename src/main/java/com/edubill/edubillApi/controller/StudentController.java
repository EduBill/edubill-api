package com.edubill.edubillApi.controller;


import com.edubill.edubillApi.dto.group.*;
import com.edubill.edubillApi.dto.student.*;
import com.edubill.edubillApi.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(summary = "반 상세 조회",
            description = "특정한 반의 정보를 조회한다.")
    @GetMapping("/groups/{groupId}")
    public ResponseEntity<GroupInfoResponseDto> findGroupDetail(@PathVariable long groupId) {
        return ResponseEntity.ok(studentService.findGroupDetailById(groupId));
    }

    @Operation(summary = "모든 반 조회",
            description = "유저가 생성한 모든 반을 가져온다.")
    @GetMapping("/allGroups")
    public ResponseEntity<Page<GroupInfoInAddStudentResponseDto>> findAllGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(studentService.findAllGroupsByUserId(pageable));
    }

    @GetMapping("/allStudents")
    @Operation(
            summary = "모든 학생 조회",
            description = "유저가 생성한 모든 학생을 조회한다.",
            parameters = {
                    @Parameter(name = "isUnpaid",
                            description = "미납입자 조회 여부. 포맷은 다음과 같다: true (소문자)",
                            required = false,
                            example = "true",
                            schema = @Schema(type = "string", defaultValue = "false")),
                    @Parameter(name = "page",
                            description = "요청 페이지 번호 (0부터 시작)",
                            required = false,
                            example = "0",
                            schema = @Schema(type = "integer", defaultValue = "0")),
                    @Parameter(name = "size",
                            description = "페이지당 데이터 수",
                            required = false,
                            example = "10",
                            schema = @Schema(type = "integer", defaultValue = "10"))
            })
    public ResponseEntity<Page<StudentAndGroupResponseDto>> findAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") String isUnpaid) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(studentService.findAllStudentsByUserId(pageable, isUnpaid));
    }

    @GetMapping("/filter/students")
    @Operation(
            summary = "학생 조회 필터링",
            description = "유저가 생성한 학생들 중 특정 조건에 맞는 학생들만 조회한다.",
            parameters = {
                    @Parameter(name = "isUnpaid",
                            description = "미납입자 조회 여부. 포맷은 다음과 같다: true (소문자)",
                            required = false,
                            example = "true",
                            schema = @Schema(type = "string", defaultValue = "false")),
                    @Parameter(name = "groupId",
                            description = "반 ID. 포맷은 다음과 같다: 1",
                            required = false,
                            example = "1",
                            schema = @Schema(type = "Long", defaultValue = "")),
                    @Parameter(name = "nameOrPhoneNum",
                            description = "조회하고 싶은 학생 이름 또는 전화번호. 포맷은 다음과 같다: 홍길동 OR 0101245678",
                            required = false,
                            example = "홍길동 OR 0101245678",
                            schema = @Schema(type = "string", defaultValue = "")),
                    @Parameter(name = "page",
                            description = "요청 페이지 번호 (0부터 시작)",
                            required = false,
                            example = "0",
                            schema = @Schema(type = "integer", defaultValue = "0")),
                    @Parameter(name = "size",
                            description = "페이지당 데이터 수",
                            required = false,
                            example = "10",
                            schema = @Schema(type = "integer", defaultValue = "10"))
            })
    public ResponseEntity<Page<StudentAndGroupResponseDto>> findStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") String isUnpaid,
            @RequestParam Long groupId,
            @RequestParam String nameOrPhoneNum) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(studentService.findStudentsByUserIdAndGroupIdOrNameOrPhoneNum(pageable, isUnpaid, groupId, nameOrPhoneNum));
    }

    @Operation(summary = "학생 상세 조회",
    description = "학생 ID 로 학생 상세 조회한다.")
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentInfoDetailResponse> findStudentDetail(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getStudentInfo(studentId));
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
