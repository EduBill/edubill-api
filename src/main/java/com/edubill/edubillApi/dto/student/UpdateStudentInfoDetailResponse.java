package com.edubill.edubillApi.dto.student;

import com.edubill.edubillApi.domain.Student;
import lombok.Getter;

import java.util.List;


@Getter
public class UpdateStudentInfoDetailResponse {
    private final Long studentId;
    private final String studentName;
    private final String studentPhoneNumber;
    private final String parentName;
    private final String parentPhoneNumber;
    private final List<GroupInfo> groups;
    private final String schoolLevel;
    private final String grade;
    private final String department;
    private final String schoolName;
    private final String memo;

    protected UpdateStudentInfoDetailResponse(Long studentId, String studentName, String studentPhoneNumber, String parentName, String parentPhoneNumber, List<GroupInfo> groups, String schoolLevel, String grade, String department, String schoolName, String memo) {
        this.studentId = studentId;
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

    public static UpdateStudentInfoDetailResponse of(Student student) {

        final List<GroupInfo> groups = student.getStudentGroups()
                .stream()
                .map(studentGroup -> GroupInfo.create(studentGroup.getGroup()))
                .toList();

        final String memo = student.getMemo() != null ? student.getMemo() : null;

        return new UpdateStudentInfoDetailResponse(
                student.getId(),
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
