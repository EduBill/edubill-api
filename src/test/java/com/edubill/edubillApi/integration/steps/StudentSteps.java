package com.edubill.edubillApi.integration.steps;

import com.edubill.edubillApi.domain.enums.DepartmentType;
import com.edubill.edubillApi.domain.enums.GradeLevel;
import com.edubill.edubillApi.domain.enums.SchoolType;
import com.edubill.edubillApi.dto.student.StudentInfoRequestDto;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.List;

public class StudentSteps {

    public static StudentInfoRequestDto 학생모델_생성(String studentName, List<Long> groupIds) {
        return StudentInfoRequestDto.builder()
                .studentName(studentName)
                .studentPhoneNumber("01012341234")
                .departmentType(DepartmentType.LIBERAL_ARTS)
                .schoolName("서울고등학교")
                .memo("진도 매우 느림")
                .gradeLevel(GradeLevel.SIXTH)
                .schoolType(SchoolType.HIGH)
                .parentPhoneNumber("01098769876")
                .parentName("p1")
                .groupIds(groupIds)
                .build();
    }

    public static ExtractableResponse<Response> 학생생성_요청(final StudentInfoRequestDto request, final String authorizationToken) {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", authorizationToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/v1/student")
                .then().log().all()
                .extract();

        return response;
    }

    public static ExtractableResponse<Response> 학생_상세조회_요청(final Long studentId, final String authorizationToken) {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", authorizationToken)
                .when()
                .get("/v1/student/{studentId}", studentId)
                .then().log().all()
                .extract();

        return response;
    }
}
