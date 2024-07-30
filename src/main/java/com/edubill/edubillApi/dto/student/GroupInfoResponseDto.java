package com.edubill.edubillApi.dto.student;

import com.edubill.edubillApi.domain.ClassTime;
import com.edubill.edubillApi.domain.Group;

import com.edubill.edubillApi.domain.enums.DayOfWeek;
import com.edubill.edubillApi.domain.enums.GradeLevel;
import com.edubill.edubillApi.domain.enums.SchoolType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupInfoResponseDto {

    private Long groupId;
    private String groupName;
    private SchoolType schoolType;
    private GradeLevel gradeLevel;
    private List<ClassTimeResponseDto> classTimeResponseDtos;
    private Integer tuition;
    private String groupMemo;
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ClassTimeResponseDto {
        private Long classTimeId;
        private DayOfWeek dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;

        public static ClassTimeResponseDto createClassTimeResponse(ClassTime classTime) {
            return new ClassTimeResponseDto(
                    classTime.getClassTimeId(),
                    classTime.getDayOfWeek(),
                    classTime.getStartTime(),
                    classTime.getEndTime()
            );
        }
    }

    public static GroupInfoResponseDto createGroupInfoResponse(Group group, List<ClassTime> classTimes) {
        List<ClassTimeResponseDto> classTimeResponseDtos = classTimes.stream()
                .map(ClassTimeResponseDto::createClassTimeResponse)
                .collect(Collectors.toList());

        return new GroupInfoResponseDto(
                group.getId(),
                group.getGroupName(),
                group.getSchoolType(),
                group.getGradeLevel(),
                classTimeResponseDtos,
                group.getTuition(),
                group.getGroupMemo()
        );
    }
}
