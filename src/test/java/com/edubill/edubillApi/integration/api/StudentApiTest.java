package com.edubill.edubillApi.integration.api;

import com.edubill.edubillApi.dto.student.GroupInfo;
import com.edubill.edubillApi.integration.IntegrationTest;
import com.edubill.edubillApi.integration.steps.GroupSteps;
import com.edubill.edubillApi.integration.steps.StudentSteps;
import com.edubill.edubillApi.integration.steps.UserSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StudentApiTest extends IntegrationTest {


    @Test
    @DisplayName("학생 조회 요청 API")
    void 학생상세조회() {
        // given
        String phoneNumber = "01012345678";
        String requestId = UserSteps.핸드폰인증후_회원가입_요청(phoneNumber);
        var loginRequest = UserSteps.로그인모델_생성(requestId, phoneNumber);
        var loginResponse = UserSteps.로그인_요청(loginRequest);
        var groupRequest1 = GroupSteps.그룹모델_생성("기초 회화반");
        var groupRequest2 = GroupSteps.그룹모델_생성("기초반");
        var group1 = GroupSteps.그룹생성_요청(groupRequest1, loginResponse.header("Authorization"));
        var group2 = GroupSteps.그룹생성_요청(groupRequest2, loginResponse.header("Authorization"));

        List<Long> groupIds = List.of(group1.jsonPath().getLong("groupId"), group2.jsonPath().getLong("groupId"));

        // when
        var studentRequest = StudentSteps.학생모델_생성("s1",groupIds);
        var studentResponse = StudentSteps.학생생성_요청(studentRequest, loginResponse.header("Authorization"));

        var response = StudentSteps.학생_상세조회_요청(studentResponse.jsonPath().getLong("studentId"), loginResponse.header("Authorization"));
        // then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("학생 정보 업데이트 API")
    void 학생_정보_업데이트() {
        // given
        String phoneNumber = "01012345678";
        String requestId = UserSteps.핸드폰인증후_회원가입_요청(phoneNumber);
        var loginRequest = UserSteps.로그인모델_생성(requestId, phoneNumber);
        var loginResponse = UserSteps.로그인_요청(loginRequest);
        var groupRequest1 = GroupSteps.그룹모델_생성("기초 회화반");
        var groupRequest2 = GroupSteps.그룹모델_생성("기초반");
        var groupRequest3 = GroupSteps.그룹모델_생성("기초 회화반3");
        var groupRequest4 = GroupSteps.그룹모델_생성("기초반3");
        var group1 = GroupSteps.그룹생성_요청(groupRequest1, loginResponse.header("Authorization"));
        var group2 = GroupSteps.그룹생성_요청(groupRequest2, loginResponse.header("Authorization"));
        var group3 = GroupSteps.그룹생성_요청(groupRequest3, loginResponse.header("Authorization"));
        var group4 = GroupSteps.그룹생성_요청(groupRequest4, loginResponse.header("Authorization"));

        List<Long> groupIds = List.of(group1.jsonPath().getLong("groupId"), group2.jsonPath().getLong("groupId"));

        // when
        var studentRequest = StudentSteps.학생모델_생성("s1",groupIds);
        var studentResponse = StudentSteps.학생생성_요청(studentRequest, loginResponse.header("Authorization"));

        var response = StudentSteps.학생_상세조회_요청(studentResponse.jsonPath().getLong("studentId"), loginResponse.header("Authorization"));

        var updateStudentRequest = StudentSteps.학생정보변경모델_생성(
                Arrays.asList(
                        new GroupInfo("기초 회화반3"),
                        new GroupInfo("기초반3")));

        var updateResponse = StudentSteps.학생_정보변경_요청(studentResponse.jsonPath().getLong("studentId"),updateStudentRequest, loginResponse.header("Authorization"));
        // then
        assertThat(updateResponse.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("그룹 생성 API")
    void 그룹생성() {
        // given
        String phoneNumber = "01012345678";
        String requestId = UserSteps.핸드폰인증후_회원가입_요청(phoneNumber);
        var loginRequest = UserSteps.로그인모델_생성(requestId, phoneNumber);
        var loginResponse = UserSteps.로그인_요청(loginRequest);

        // when
        var groupRequest = GroupSteps.그룹모델_생성("기초 회화반");
        var response = GroupSteps.그룹생성_요청(groupRequest, loginResponse.header("Authorization"));

        // then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("학생 생성 요청 API")
    void 학생생성() {
        // given
        String phoneNumber = "01012345678";
        String requestId = UserSteps.핸드폰인증후_회원가입_요청(phoneNumber);
        var loginRequest = UserSteps.로그인모델_생성(requestId, phoneNumber);
        var loginResponse = UserSteps.로그인_요청(loginRequest);
        var groupRequest1 = GroupSteps.그룹모델_생성("기초 회화반");
        var groupRequest2 = GroupSteps.그룹모델_생성("기초반");
        var group1 = GroupSteps.그룹생성_요청(groupRequest1, loginResponse.header("Authorization"));
        var group2 = GroupSteps.그룹생성_요청(groupRequest2, loginResponse.header("Authorization"));

        List<Long> groupIds = List.of(group1.jsonPath().getLong("groupId"), group2.jsonPath().getLong("groupId"));

        // when
        var studentRequest = StudentSteps.학생모델_생성("s1",groupIds);
        var response = StudentSteps.학생생성_요청(studentRequest, loginResponse.header("Authorization"));

        // then
        assertThat(response.statusCode()).isEqualTo(200);
    }

}