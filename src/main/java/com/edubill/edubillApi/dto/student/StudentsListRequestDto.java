package com.edubill.edubillApi.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentsListRequestDto {
    @Schema(description = "그룹 ids", type = "List<Long>", example = "{1, 2, 3} OR null")
    private List<Long> groupIds;

    @Schema(description = "미납입 학생만 조회 여부", type = "string", example = "true", defaultValue = "false")
    @NotNull(message = "미납입 조회 여부는 필수입니다.")
    private Boolean isUnpaid;

    @Schema(description = "학생이름 또는 학생전화번호", type = "String", example = "학생1 OR 01012345678 OR 빈 문자열(\"\") ")
    private String studentNameORPhoneNum;

    @Schema(description = "정렬기준", type = "String", example = "id", defaultValue = " studentName(가나다순) || id(최신등록순)")
    @NotNull(message = "정렬 기준은 필수입니다.")
    private String sort;
}
