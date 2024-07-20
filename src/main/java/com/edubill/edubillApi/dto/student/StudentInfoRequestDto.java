package com.edubill.edubillApi.dto.student;

import com.edubill.edubillApi.domain.enums.DepartmentType;
import com.edubill.edubillApi.domain.enums.GradeLevel;
import com.edubill.edubillApi.domain.enums.SchoolType;
import com.edubill.edubillApi.domain.StudentGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentInfoRequestDto {
    @Schema(description = "studentName", type = "String", example = "학생1")
    @NotNull(message = "학생이름은 필수입니다.")
    private String studentName;

    @Schema(description = "studentPhoneNumber", type = "String", example = "01012341234")
    @NotNull(message = "학생전화번호는 필수입니다.")
    private String studentPhoneNumber;

    @Schema(description = "parentName", type = "String", example = "부모1")
    @NotNull(message = "학부모이름은 필수입니다.")
    private String parentName;

    @Schema(description = "parentPhoneNumber", type = "String", example = "01098769876")
    @NotNull(message = "학부모전화번호는 필수입니다.")
    private String parentPhoneNumber;

    private List<Long> studentGroupIds;

    private SchoolType schoolType;
    private GradeLevel gradeLevel;
    private DepartmentType departmentType;
    private String schoolName;
    private String memo;
}
