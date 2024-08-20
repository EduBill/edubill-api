package com.edubill.edubillApi.dto.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentAndGroupResponseDto {
    private Long studentId;
    private String studentName;
    private String parentName;
    private List<String> classNames = new ArrayList<>();
}
