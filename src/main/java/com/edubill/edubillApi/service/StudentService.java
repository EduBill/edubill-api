package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.Group;
import com.edubill.edubillApi.domain.ClassTime;
import com.edubill.edubillApi.domain.Student;
import com.edubill.edubillApi.domain.StudentGroup;
import com.edubill.edubillApi.dto.student.StudentGroupInfoRequestDto;
import com.edubill.edubillApi.dto.student.StudentInfoRequestDto;
import com.edubill.edubillApi.dto.student.StudentInfoResponseDto;
import com.edubill.edubillApi.dto.student.StudentInfoTestRequestDto;
import com.edubill.edubillApi.error.exception.GroupNotFoundException;
import com.edubill.edubillApi.repository.StudentGroupRepository;
import com.edubill.edubillApi.repository.group.GroupRepository;
import com.edubill.edubillApi.repository.ClassTimeRepository;
import com.edubill.edubillApi.repository.studentgroup.StudentGroupRepository;
import com.edubill.edubillApi.repository.student.StudentRepository;

import com.edubill.edubillApi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final ClassTimeRepository classTimeRepository;

    @Transactional
    public StudentInfoResponseDto addStudentInfo(StudentInfoRequestDto studentInfoRequestDto) {
        Student student = Student.from(studentInfoRequestDto);
        studentRepository.save(student);

        List<StudentGroup> studentGroups = new ArrayList<>();
        for (Long groupId : studentInfoRequestDto.getGroupIds()) {
            Group group = groupRepository.findById(groupId) //TODO: groupId와 userId를 동시에 이용해 찾아야 하는지
                    .orElseThrow(() -> new GroupNotFoundException("Group not found with id " + groupId));

            group.addStudent();

            StudentGroup studentGroup = StudentGroup.builder()
                    .student(student)
                    .group(group)
                    .build();
            studentGroup.setStudent(student); // Student 설정
            studentGroup.setGroup(group);   // Group 설정
            studentGroups.add(studentGroup);
        }
        studentGroupRepository.saveAll(studentGroups);

        return StudentInfoResponseDto.from(student, studentInfoRequestDto.getGroupIds());
    }

    @Transactional
    public void addStudentGroupInfo(StudentGroupInfoRequestDto studentGroupInfoRequestDto) {
        String userId = SecurityUtils.getCurrentUserId();
        List<StudentGroupInfoRequestDto.ClassTimeDto> classTimeDtos = studentGroupInfoRequestDto.getClassTimeDtos();

        StudentGroup savedStudentGroup = studentGroupRepository.save(StudentGroup.builder()
                .groupName(studentGroupInfoRequestDto.getGroupName())
                .managerId(userId)
                .tuition(studentGroupInfoRequestDto.getTuition())
                .schoolType(studentGroupInfoRequestDto.getSchoolType())
                .gradeLevel(studentGroupInfoRequestDto.getGradeLevel())
                .build());

        // DTO를 도메인 객체로 변환
        List<ClassTime> classTimes = classTimeDtos.stream()
                .map(dto -> {
                    // ClassTime 객체를 생성
                    ClassTime classTime = ClassTime.builder()
                            .dayOfWeek(dto.getDayOfWeek())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .build();
                    // 연관관계 설정
                    classTime.setStudentGroup(savedStudentGroup);
                    return classTime;
                })
                .collect(Collectors.toList());
        classTimeRepository.saveAll(classTimes);
    }


    @Transactional
    public void addStudentInfoTest(StudentInfoTestRequestDto studentInfoTestRequestDto, final String userId) {

        List<Group> groups = groupRepository.getGroupsByUserId(userId);
        Group group = null;
        if (!groups.isEmpty()) {
            group = groups.get(0); //TODO: student_group이 여러개일 경우 한개만 선택하도록 student_group_id를 input 받도록 수정
        } else {
            group = Group.builder()
                    .groupName("중등 B반")
                    .managerId(userId)
                    .tuition(300000)
                    .build();
        }
        //학생수 증가
        group.addStudent();
        Student student = Student.builder()
                .studentName(studentInfoTestRequestDto.getStudentName())
                .studentPhoneNumber(studentInfoTestRequestDto.getStudentPhoneNumber())
                .parentName(studentInfoTestRequestDto.getParentName())
                .parentPhoneNumber(studentInfoTestRequestDto.getParentPhoneNumber())
                .build();
        studentRepository.save(student);

        StudentGroup studentGroup = StudentGroup.builder()
                .build(); // 빈 상태로 생성
        studentGroup.setStudent(student); // Student 설정
        studentGroup.setGroup(group);   // Group 설정

        studentGroupRepository.save(studentGroup);
        groupRepository.save(group);
    }
}
