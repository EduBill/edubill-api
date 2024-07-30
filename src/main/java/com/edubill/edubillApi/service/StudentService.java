package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.Group;
import com.edubill.edubillApi.domain.ClassTime;
import com.edubill.edubillApi.domain.Student;
import com.edubill.edubillApi.domain.StudentGroup;
import com.edubill.edubillApi.dto.student.*;
import com.edubill.edubillApi.error.exception.GroupNotFoundException;
import com.edubill.edubillApi.repository.StudentGroupRepository;
import com.edubill.edubillApi.repository.group.GroupRepository;
import com.edubill.edubillApi.repository.ClassTimeRepository;

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

        Student savedStudent = studentRepository.save(Student.from(studentInfoRequestDto));

        List<StudentGroup> studentGroups = new ArrayList<>();
        for (Long groupId : studentInfoRequestDto.getGroupIds()) {
            Group group = groupRepository.findById(groupId) //TODO: groupId와 userId를 동시에 이용해 찾아야 하는지
                    .orElseThrow(() -> new GroupNotFoundException("Group not found with id " + groupId));

            group.addStudent();

            StudentGroup studentGroup = StudentGroup.builder()
                    .student(savedStudent)
                    .group(group)
                    .build();
            studentGroup.setStudent(savedStudent); // Student 설정
            studentGroup.setGroup(group);   // Group 설정
            studentGroups.add(studentGroup);
        }
        studentGroupRepository.saveAll(studentGroups);

        return StudentInfoResponseDto.createStudentInfoResponse(savedStudent, studentInfoRequestDto.getGroupIds());
    }

    @Transactional
    public GroupInfoResponseDto addGroupInfo(GroupInfoRequestDto groupInfoRequestDto) {
        String userId = SecurityUtils.getCurrentUserId();
        List<GroupInfoRequestDto.ClassTimeRequestDto> classTimeRequestDtos = groupInfoRequestDto.getClassTimeRequestDtos();

        Group savedGroup = groupRepository.save(Group.from(groupInfoRequestDto, userId));

        // DTO를 도메인 객체로 변환
        List<ClassTime> classTimes = classTimeRequestDtos.stream()
                .map(dto -> {
                    // ClassTime 객체를 생성
                    ClassTime classTime = ClassTime.builder()
                            .dayOfWeek(dto.getDayOfWeek())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .build();
                    // 연관관계 설정
                    classTime.setGroup(savedGroup);
                    return classTime;
                })
                .collect(Collectors.toList());
        classTimeRepository.saveAll(classTimes);

        return GroupInfoResponseDto.createGroupInfoResponse(savedGroup, classTimes);
    }


    @Transactional
    public void addStudentAndGroupInfoTest(StudentInfoTestRequestDto studentInfoTestRequestDto, final String userId) {

        List<Group> groups = groupRepository.getGroupsByUserId(userId);
        Group group = null;
        if (!groups.isEmpty()) {
            group = groups.get(0);
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
