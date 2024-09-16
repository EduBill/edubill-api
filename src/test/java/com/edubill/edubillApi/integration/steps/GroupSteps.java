package com.edubill.edubillApi.integration.steps;

import com.edubill.edubillApi.domain.enums.DayOfWeek;
import com.edubill.edubillApi.domain.enums.GradeLevel;
import com.edubill.edubillApi.domain.enums.SchoolType;
import com.edubill.edubillApi.dto.group.GroupInfoRequestDto;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

public class GroupSteps {

    public static GroupInfoRequestDto 그룹모델_생성(String groupName) {

        GroupInfoRequestDto.ClassTimeRequestDto classTimeRequestDto1 = createClassTimeRequestDto(DayOfWeek.MON, "14:00", "18:00");
        GroupInfoRequestDto.ClassTimeRequestDto classTimeRequestDto2 = createClassTimeRequestDto(DayOfWeek.FRI, "14:00", "18:00");

        List<GroupInfoRequestDto.ClassTimeRequestDto> classTimeRequestDtos = List.of(classTimeRequestDto1, classTimeRequestDto2);

        return GroupInfoRequestDto.builder()
                .groupName(groupName)
                .groupMemo("반 메모")
                .tuition(100000)
                .schoolType(SchoolType.HIGH)
                .gradeLevel(GradeLevel.THIRD)
                .classTimeRequestDtos(classTimeRequestDtos)
                .build();
    }

    public static ExtractableResponse<Response> 그룹생성_요청(GroupInfoRequestDto groupInfoRequestDto, String authorizationToken) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", authorizationToken)
                .body(groupInfoRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/v1/student/groups")
                .then().log().all()
                .extract();

        return response;
    }

    private static  GroupInfoRequestDto.ClassTimeRequestDto createClassTimeRequestDto(DayOfWeek dayOfWeek, String startTime, String endTime) {
        return GroupInfoRequestDto.ClassTimeRequestDto.builder()
                .dayOfWeek(dayOfWeek)
                .startTime(LocalTime.parse(startTime))
                .endTime(LocalTime.parse(endTime))
                .build();
    }
}
