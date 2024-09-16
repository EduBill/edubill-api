package com.edubill.edubillApi.integration.api;

import com.edubill.edubillApi.integration.IntegrationTest;
import com.edubill.edubillApi.integration.steps.GroupSteps;
import com.edubill.edubillApi.integration.steps.StudentSteps;
import com.edubill.edubillApi.integration.steps.UserSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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