package com.edubill.edubillApi.dto.group;


import com.edubill.edubillApi.domain.enums.DayOfWeek;
import com.edubill.edubillApi.domain.enums.GradeLevel;
import com.edubill.edubillApi.domain.enums.SchoolType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
public class GroupInfoRequestDto {

    @Schema(description = "반 이름", type = "String", example = "기초반")
    @NotNull(message = "반 이름은 필수입니다.")
    private String groupName;

    @Schema(description = "수업대상: 학교분류", type = "String", example = "초등학교")
    @NotNull(message = "학교 분류는 필수입니다.")
    private SchoolType schoolType;

    @Schema(description = "수업대상: 학년", type = "String", example = "1학년")
    @NotNull(message = "학년은 필수입니다.")
    private GradeLevel gradeLevel;

    private List<ClassTimeRequestDto> classTimeRequestDtos;

    @Schema(description = "수업료", type = "long", example = "300000")
    @NotNull(message = "수업료는 필수입니다.")
    private Integer tuition;

    @Schema(description = "반 메모", type = "String", example = "반 메모")
    private String groupMemo;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ClassTimeRequestDto {

        @Schema(description = "수업시간: 날짜", type = "String", example = "화")
        @NotNull(message = "날짜입력은 필수입니다.")
        private DayOfWeek dayOfWeek;

        @Schema(description = "수업시간: 시작시간", type = "String", example = "14:00")
        @NotNull(message = "시작시간 입력은 필수입니다.")
        private LocalTime startTime;

        @Schema(description = "수업시간: 종료시간", type = "String", example = "18:00")
        @NotNull(message = "종료시간 입력은 필수입니다.")
        private LocalTime endTime;
    }
}