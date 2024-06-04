package com.edubill.edubillApi.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentInfoRequestDto {

    @Schema(description = "studentName", type = "String", example = "학생1")
    @NotNull(message = "학생이름은 필수입니다.")
    private String studentName;

    @Schema(description = "studentPhoneNumber", type = "String", example = "01011111111")
    @NotNull(message = "학생전화번호는 필수입니다.")
    private String studentPhoneNumber;

    @Schema(description = "parentName", type = "String", example = "부모1")
    @NotNull(message = "학부모이름은 필수입니다.")
    private String parentName;

    @Schema(description = "parentPhoneNumber", type = "String", example = "01011112222")
    @NotNull(message = "학부모전화번호는 필수입니다.")
    private String parentPhoneNumber;
}
