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
    @Schema(description = "groupIds", type = "List<Long>", example = "{1, 2, 3}")
    private List<Long> groupIds;

    @Schema(description = "isUnpaid", type = "string", example = "true", defaultValue = "false")
    @NotNull(message = "미납입 조회 여부는 필수입니다.")
    private Boolean isUnpaid;

    @Schema(description = "studentName", type = "String", example = "학생1")
    private String studentName;

    @Schema(description = "studentPhoneNumber", type = "String", example = "01012345678")
    private String studentPhoneNumber;

    @Schema(description = "정렬기준 / studentName(가나다순) || id(최신등록순)", type = "String", example = "id", defaultValue = "id")
    @NotNull(message = "정렬 기준은 필수입니다.")
    private String sort;
}
