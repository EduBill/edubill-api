package com.edubill.edubillApi.dto.group;

import com.edubill.edubillApi.domain.ClassTime;
import com.edubill.edubillApi.domain.Group;
import com.edubill.edubillApi.domain.enums.DayOfWeek;
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
public class GroupInfoInAddStudentResponseDto {

    private Long groupId;
    private String groupName;
    private List<ClassTimeResponseDto> classTimeResponseDtos;

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
}
