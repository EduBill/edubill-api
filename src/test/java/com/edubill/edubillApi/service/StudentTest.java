package com.edubill.edubillApi.service;


import com.edubill.edubillApi.config.TestcontainerConfig;
import com.edubill.edubillApi.domain.Group;
import com.edubill.edubillApi.domain.Student;
import com.edubill.edubillApi.domain.StudentGroup;
import com.edubill.edubillApi.domain.enums.DepartmentType;
import com.edubill.edubillApi.domain.enums.GradeLevel;
import com.edubill.edubillApi.domain.enums.SchoolType;
import com.edubill.edubillApi.dto.student.StudentInfoDetailResponse;
import com.edubill.edubillApi.dto.student.StudentInfoResponseDto;
import com.edubill.edubillApi.error.exception.StudentNotFoundException;
import com.edubill.edubillApi.repository.StudentGroupRepository;
import com.edubill.edubillApi.repository.group.GroupRepository;
import com.edubill.edubillApi.repository.student.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestcontainerConfig
@Transactional
public class StudentTest {

     @Autowired
     StudentService studentService;
     @Autowired
     StudentRepository studentRepository;
     @Autowired
     GroupRepository groupRepository;
     @Autowired
     StudentGroupRepository studentGroupRepository;

    private Student student;
    private Group savedGroup1;
    private Group savedGroup2;

    @BeforeEach
    void setUp() {
        student = createStudent(
                "s1",
                "01012341234",
                "p1",
                "01098769876",
                "서울고등학교",
                "학업 진도 느림"
        );
        studentRepository.save(student);

        savedGroup1 = groupRepository.save(createGroup("기초 회화반", "testManager"));
        savedGroup2 = groupRepository.save(createGroup("기초반", "testManager"));

        createAndSaveStudentGroup(student, savedGroup1);
        createAndSaveStudentGroup(student, savedGroup2);
    }

    @Test
    @DisplayName("학생 ID로 학생 상세 정보를 조회한다.")
    void shouldReturnStudentDetailsWhenGivenStudentId() {
        // When: 학생의 상세 정보 조회
        final StudentInfoDetailResponse result = studentService.getStudentInfo(student.getId());

        // Then: 조회된 정보 검증
        assertStudentDetails(result);
        assertGroupDetails(result);
    }

    @Test
    @DisplayName("잘못된 학생 ID로 조회했을 경우 StudentNotFoundException을 발생한다.")
    void shouldThrowStudentNotFoundExceptionWhenInvalidStudentIdIsProvided() {
        // Given: 잘못된 아이디
        final Long invalidStudentId = -1L;

        // When, Then: Exception 검증
        assertThatThrownBy(() -> studentService.getStudentInfo(invalidStudentId))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student not found with id " + invalidStudentId);
    }

    // 학생 정보 검증
    private void assertStudentDetails(StudentInfoDetailResponse result) {
        assertThat(result.getStudentName()).isEqualTo("s1");
        assertThat(result.getStudentPhoneNumber()).isEqualTo("01012341234");
        assertThat(result.getParentName()).isEqualTo("p1");
        assertThat(result.getParentPhoneNumber()).isEqualTo("01098769876");
        assertThat(result.getSchoolName()).isEqualTo("서울고등학교");
        assertThat(result.getMemo()).isEqualTo("학업 진도 느림");
        assertThat(result.getSchoolLevel()).isEqualTo(SchoolType.HIGH.getDescription());
        assertThat(result.getDepartment()).isEqualTo(DepartmentType.LIBERAL_ARTS.getDescription());
        assertThat(result.getGrade()).isEqualTo(GradeLevel.SIXTH.getDescription());
    }

    // 그룹 정보 검증 메서드
    private void assertGroupDetails(StudentInfoDetailResponse result) {
        assertThat(result.getGroups())
                .hasSize(2)
                .extracting("groupName")
                .containsExactlyInAnyOrder("기초 회화반", "기초반");
    }

    // 학생 생성 메서드
    private Student createStudent(String studentName,
                                  String phoneNumber,
                                  String parentName,
                                  String parentPhoneNumber,
                                  String schoolName,
                                  String memo) {
        return Student.builder()
                .studentName(studentName)
                .studentPhoneNumber(phoneNumber)
                .parentName(parentName)
                .parentPhoneNumber(parentPhoneNumber)
                .schoolType(SchoolType.HIGH)
                .gradeLevel(GradeLevel.SIXTH)
                .departmentType(DepartmentType.LIBERAL_ARTS)
                .schoolName(schoolName)
                .memo(memo)
                .build();
    }

    // 그룹 생성 메서드
    private Group createGroup(String groupName, String managerId) {
        return Group.builder()
                .groupName(groupName)
                .managerId(managerId)
                .build();
    }

    // 학생 그룹 생성 및 저장 메서드
    private void createAndSaveStudentGroup(Student student, Group group) {
        StudentGroup studentGroup = createStudentGroup(student, group);
        studentGroupRepository.save(studentGroup);
    }

    // 학생-그룹 연결 생성 메서드
    private StudentGroup createStudentGroup(Student student, Group group) {
        StudentGroup studentGroup = StudentGroup.builder()
                .student(student)
                .group(group)
                .build();

        studentGroup.setGroup(group);
        studentGroup.setStudent(student);
        return studentGroup;
    }
}
