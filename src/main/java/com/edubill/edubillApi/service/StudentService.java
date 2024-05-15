package com.edubill.edubillApi.service;


import com.edubill.edubillApi.domain.Student;
import com.edubill.edubillApi.domain.StudentGroup;
import com.edubill.edubillApi.dto.StudentInfoRequestDto;
import com.edubill.edubillApi.repository.StudentGroupRepository;
import com.edubill.edubillApi.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentGroupRepository studentGroupRepository;
    private final StudentRepository studentRepository;
    @Transactional
    public void addStudentInfo(StudentInfoRequestDto studentInfoRequestDto, final String userId) {

        List<StudentGroup> studentGroups = studentGroupRepository.getUserGroupsByUserId(userId);
        StudentGroup studentGroup = null;
        if (!studentGroups.isEmpty()) {
            studentGroup = studentGroups.get(0); //TODO: studentGroup을 지정하지 않고 Academy에 모두 저장해야하는지 확인
        }
        else{
            studentGroup = StudentGroup.builder()
                    .groupName("중등 B반")
                    .managerId(userId)
                    .tuition(300000)
                    .build();
        }

        //학생수 증가
        studentGroup.addStudent();

        studentRepository.save(Student.builder()
                .studentName(studentInfoRequestDto.getStudentName())
                .studentPhoneNumber(studentInfoRequestDto.getStudentPhoneNumber())
                .parentName(studentInfoRequestDto.getParentName())
                .parentPhoneNumber(studentInfoRequestDto.getParentPhoneNumber())
                .studentGroup(studentGroup)
                .build());

        studentGroupRepository.save(studentGroup);
    }
}
