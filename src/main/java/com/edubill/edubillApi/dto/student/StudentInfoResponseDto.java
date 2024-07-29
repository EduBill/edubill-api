package com.edubill.edubillApi.dto.student;

import com.edubill.edubillApi.domain.Student;
import com.edubill.edubillApi.domain.enums.DepartmentType;
import com.edubill.edubillApi.domain.enums.GradeLevel;
import com.edubill.edubillApi.domain.enums.SchoolType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentInfoResponseDto {

    private Long studentId;

    private String studentName;

    private String studentPhoneNumber;

    private String parentName;

    private String parentPhoneNumber;

    private List<Long> groupIds;

    private SchoolType schoolType;

    private GradeLevel gradeLevel;

    private DepartmentType departmentType;

    private String schoolName;

    private String memo;

    public static StudentInfoResponseDto from(Student student, List<Long> groupIds) {
        return new StudentInfoResponseDto(
                student.getId(),
                student.getStudentName(),
                student.getStudentPhoneNumber(),
                student.getParentName(),
                student.getParentPhoneNumber(),
                groupIds,
                student.getSchoolType(),
                student.getGradeLevel(),
                student.getDepartmentType(),
                student.getSchoolName(),
                student.getMemo()
        );
    }
}
