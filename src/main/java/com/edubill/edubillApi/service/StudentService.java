package com.edubill.edubillApi.service;


import com.edubill.edubillApi.domain.Student;
import com.edubill.edubillApi.domain.StudentGroup;
import com.edubill.edubillApi.dto.student.StudentInfoTestRequestDto;
import com.edubill.edubillApi.repository.studentgroup.StudentGroupRepository;
import com.edubill.edubillApi.repository.student.StudentRepository;
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
    public void addStudentInfo(StudentInfoTestRequestDto studentInfoTestRequestDto, final String userId) {

        List<StudentGroup> studentGroups = studentGroupRepository.getStudentGroupsByUserId(userId);
        StudentGroup studentGroup = null;
        if (!studentGroups.isEmpty()) {
            studentGroup = studentGroups.get(0); //TODO: student_group이 여러개일 경우 한개만 선택하도록 student_group_id를 input 받도록 수정
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
                .studentName(studentInfoTestRequestDto.getStudentName())
                .studentPhoneNumber(studentInfoTestRequestDto.getStudentPhoneNumber())
                .parentName(studentInfoTestRequestDto.getParentName())
                .parentPhoneNumber(studentInfoTestRequestDto.getParentPhoneNumber())
                .studentGroup(studentGroup)
                .build());

        studentGroupRepository.save(studentGroup);
    }
}
