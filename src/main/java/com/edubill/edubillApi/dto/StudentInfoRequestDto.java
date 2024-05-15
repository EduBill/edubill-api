package com.edubill.edubillApi.dto;


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

    @NotNull(message = "학생이름은 필수입니다.")
    private String studentName;
    @NotNull(message = "학생전화번호는 필수입니다.")
    private String studentPhoneNumber;
    @NotNull(message = "학부모이름은 필수입니다.")
    private String parentName;
    @NotNull(message = "학부모전화번호는 필수입니다.")
    private String parentPhoneNumber;
}
