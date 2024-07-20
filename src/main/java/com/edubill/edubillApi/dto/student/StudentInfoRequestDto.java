package com.edubill.edubillApi.dto.student;

import com.edubill.edubillApi.domain.enums.DepartmentType;
import com.edubill.edubillApi.domain.enums.GradeLevel;
import com.edubill.edubillApi.domain.enums.SchoolType;
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
    @Schema(description = "학생 이름", type = "String", example = "학생1")
    @NotNull(message = "학생 이름은 필수입니다.")
    private String studentName;

    @Schema(description = "학생 전화번호", type = "String", example = "01012341234")
    @NotNull(message = "학생 전화번호는 필수입니다.")
    private String studentPhoneNumber;

    @Schema(description = "학부모 이름", type = "String", example = "부모1")
    @NotNull(message = "학부모 이름은 필수입니다.")
    private String parentName;

    @Schema(description = "학부모 전화번호", type = "String", example = "01098769876")
    @NotNull(message = "학부모 전화번호는 필수입니다.")
    private String parentPhoneNumber;

    @Schema(
            description = "학생 그룹 ID 리스트",
            type = "array",
            example = "[1, 2, 3]"
    )
    @NotNull(message = "학생 그룹 ID는 필수입니다.")
    private List<Long> studentGroupIds;

    @Schema(description = "학교급", type = "String", example = "초등학교", allowableValues = {"초등학교", "중학교", "고등학교"})
    private SchoolType schoolType;

    @Schema(description = "학년", type = "String", example = "1학년")
    private GradeLevel gradeLevel;

    @Schema(description = "계열", type = "String", example = "문과", allowableValues = {"문과", "이과", "예체능"})
    private DepartmentType departmentType;

    @Schema(description = "학교이름", type = "String", example = "에듀빌초등학교")
    private String schoolName;

    @Schema(description = "메모", type = "String")
    private String memo;
}
