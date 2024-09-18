package com.edubill.edubillApi.dto.student;


import com.edubill.edubillApi.domain.Student;
import lombok.Getter;

import java.util.List;


@Getter
public class StudentInfoDetailResponse {

    private final String studentName;
    private final String studentPhoneNumber;
    private final String parentName;
    private final String parentPhoneNumber;
    private final List<GroupInfoDetailResponse> groups;
    private final String schoolLevel;
    private final String grade;
    private final String department;
    private final String schoolName;
    private final String memo;

    private StudentInfoDetailResponse(String studentName, String studentPhoneNumber, String parentName, String parentPhoneNumber, List<GroupInfoDetailResponse> groups,
                                      String schoolLevel, String grade, String department, String schoolName, String memo) {
        this.studentName = studentName;
        this.studentPhoneNumber = studentPhoneNumber;
        this.parentName = parentName;
        this.parentPhoneNumber = parentPhoneNumber;
        this.groups = groups;
        this.schoolLevel = schoolLevel;
        this.grade = grade;
        this.department = department;
        this.schoolName = schoolName;
        this.memo = memo;
    }

    public static StudentInfoDetailResponse of(Student student) {

        final List<GroupInfoDetailResponse> groups = student.getStudentGroups()
                .stream()
                .map(studentGroup -> GroupInfoDetailResponse.create(studentGroup.getGroup()))
                .toList();

        final String memo = student.getMemo() != null ? student.getMemo() : null;

        return new StudentInfoDetailResponse(
                student.getStudentName(),
                student.getStudentPhoneNumber(),
                student.getParentName(),
                student.getParentPhoneNumber(),
                groups,
                student.getSchoolType().getDescription(),
                student.getGradeLevel().getDescription(),
                student.getDepartmentType().getDescription(),
                student.getSchoolName(),
                memo
        );
    }


}

