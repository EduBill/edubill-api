package com.edubill.edubillApi.dto.student;


import com.edubill.edubillApi.domain.enums.DayOfWeek;
import com.edubill.edubillApi.domain.enums.GradeLevel;
import com.edubill.edubillApi.domain.enums.SchoolType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentGroupInfoRequestDto {

    private String groupName;

    private SchoolType schoolType;

    private GradeLevel gradeLevel;

    private List<ClassTimeDto> classTimeDtos;

    private int tuition;

    private String memo;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ClassTimeDto {

        private DayOfWeek dayOfWeek;

        private LocalTime startTime;

        private LocalTime endTime;
    }
}
