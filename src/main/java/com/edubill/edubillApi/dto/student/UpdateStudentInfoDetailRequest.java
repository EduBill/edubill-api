package com.edubill.edubillApi.dto.student;

import com.edubill.edubillApi.domain.enums.DepartmentType;
import com.edubill.edubillApi.domain.enums.GradeLevel;
import com.edubill.edubillApi.domain.enums.SchoolType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class UpdateStudentInfoDetailRequest {
    private String studentName;
    private String studentPhoneNumber;
    private String parentName;
    private String parentPhoneNumber;
    private List<GroupInfo> groups;
    private SchoolType schoolLevel;
    private GradeLevel grade;
    private DepartmentType department;
    private String schoolName;
    private String memo;

    public UpdateStudentInfoDetailRequest(String studentName, String studentPhoneNumber, String parentName, String parentPhoneNumber, List<GroupInfo> groups, SchoolType schoolLevel, GradeLevel grade, DepartmentType department, String schoolName, String memo) {
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

}
