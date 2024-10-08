package com.edubill.edubillApi.service;


import com.edubill.edubillApi.config.TestcontainerConfig;
import com.edubill.edubillApi.domain.ClassTime;
import com.edubill.edubillApi.domain.Group;
import com.edubill.edubillApi.domain.Student;
import com.edubill.edubillApi.domain.StudentGroup;
import com.edubill.edubillApi.domain.enums.DayOfWeek;
import com.edubill.edubillApi.domain.enums.DepartmentType;
import com.edubill.edubillApi.domain.enums.GradeLevel;
import com.edubill.edubillApi.domain.enums.SchoolType;
import com.edubill.edubillApi.dto.group.GroupInfoRequestDto;
import com.edubill.edubillApi.dto.group.GroupInfoResponseDto;
import com.edubill.edubillApi.dto.student.StudentInfoDetailResponse;
import com.edubill.edubillApi.dto.student.StudentInfoResponseDto;
import com.edubill.edubillApi.error.exception.StudentNotFoundException;
import com.edubill.edubillApi.repository.ClassTimeRepository;
import com.edubill.edubillApi.repository.StudentGroupRepository;
import com.edubill.edubillApi.repository.group.GroupRepository;
import com.edubill.edubillApi.repository.student.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     @Autowired
     ClassTimeRepository classTimeRepository;

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

        List<ClassTime> classTimes = new ArrayList<>();
        classTimes.add(classTimeRepository.save(ClassTime.builder()
                .dayOfWeek(DayOfWeek.MON)
                .startTime(LocalTime.of(14,00))
                .endTime(LocalTime.of(16,00))
                .build()));
        classTimes.add(classTimeRepository.save(ClassTime.builder()
                .dayOfWeek(DayOfWeek.WED)
                .startTime(LocalTime.of(14,00))
                .endTime(LocalTime.of(16,00))
                .build()));
        savedGroup1 = groupRepository.save(createGroup("기초 회화반", "testManager", classTimes));
        savedGroup2 = groupRepository.save(createGroup("기초반", "testManager", classTimes));

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

    @Test
    @DisplayName("반 정보를 변경한다. - 반 이름, schoolType, gradeLevel, 수업료, 메모, 수업시간")
    void updateGroupInfo(){

        // given
        List<GroupInfoRequestDto.ClassTimeRequestDto> classTimes = new ArrayList<>();
        classTimes.add(GroupInfoRequestDto.ClassTimeRequestDto.builder()
                .dayOfWeek(DayOfWeek.MON)
                .startTime(LocalTime.of(14,00))
                .endTime(LocalTime.of(16,00))
                .build());
        classTimes.add(GroupInfoRequestDto.ClassTimeRequestDto.builder()
                .dayOfWeek(DayOfWeek.THU)
                .startTime(LocalTime.of(14,00))
                .endTime(LocalTime.of(16,00))
                .build());

        GroupInfoRequestDto groupInfoRequestDto = GroupInfoRequestDto.builder()
                .groupName("changedName")
                .schoolType(SchoolType.HIGH)
                .gradeLevel(GradeLevel.FIRST)
                .tuition(300000)
                .groupMemo("Change")
                .classTimeRequestDtos(classTimes)
                .build();

        // when
        GroupInfoResponseDto groupInfoResponseDto = studentService.updateGroupInfo(savedGroup1.getId(), groupInfoRequestDto);

        // then
        Assertions.assertThat(groupInfoResponseDto.getGroupName()).isEqualTo("changedName");
        Assertions.assertThat(groupInfoResponseDto.getSchoolType()).isEqualTo(SchoolType.HIGH);
        Assertions.assertThat(groupInfoResponseDto.getGradeLevel()).isEqualTo(GradeLevel.FIRST);
        Assertions.assertThat(groupInfoResponseDto.getTuition()).isEqualTo(300000);
        Assertions.assertThat(groupInfoResponseDto.getGroupMemo()).isEqualTo("Change");
        Assertions.assertThat(groupInfoResponseDto.getClassTimeResponseDtos().size()).isEqualTo(2);
        Assertions.assertThat(groupInfoResponseDto.getClassTimeResponseDtos().get(0).getDayOfWeek()).isEqualTo(DayOfWeek.MON);
        Assertions.assertThat(groupInfoResponseDto.getClassTimeResponseDtos().get(1).getDayOfWeek()).isEqualTo(DayOfWeek.THU);
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
    private Group createGroup(String groupName, String managerId, List<ClassTime> classTime) {
        return Group.builder()
                .groupName(groupName)
                .managerId(managerId)
                .classTimes(classTime)
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
