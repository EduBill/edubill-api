package com.edubill.edubillApi.exception;

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
    LOGIN_INPUT_INVALID(400, "U002","입력하신 전화번호가 일치하지 않습니다.");


    private int status;
    private final String code;
    private final String message;
}
