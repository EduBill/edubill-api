package com.edubill.edubillApi.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT) //해당객체를 json으로 직렬화
public enum ErrorCode {

    // 프론트에 보여지는 값
    // User
    USER_DUPLICATION(400, "U001","이미 존재하는 회원입니다."),
    LOGIN_INPUT_INVALID(400, "U002","입력하신 전화번호가 일치하지 않습니다."),
    USER_NOT_FOUND(400, "U003", "존재하지 않는 사용자입니다."),
    NO_AUTHORITY(403, "U004", "인증되지 않은 사용자입니다."),
    NEED_LOGIN(401, "U005", "로그인이 필요합니다."),
    AUTHENTICATION_NOT_FOUND(401, "U006", "Security Context에 인증 정보가 없습니다."),
    USER_ALREADY_LOGOUT(400, "U007", "사용자가 이미 로그아웃하였습니다."),
    INVALID_VERIFY_NUMBER(401,"U008", "인증번호가 일치하지 않습니다."),

    // Academy
    ACADEMY_NOT_FOUND(400, "A001", "학원이 존재하지 않습니다."),

    // common
    INTERNAL_SERVER_ERROR(500, "C001", "서버 오류"),
    INVALID_INPUT_VALUE(400, "C002", "잘못된 일력 값을 입력하였습니다."),
    INVALID_TYPE_VALUE(400, "C003", "잘못된 타입을 입력하였습니다."),
    BAD_CREDENTIALS(400, "C005", "bad credentials");


    private final int status;
    private final String code;
    private final String message;
}