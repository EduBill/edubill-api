package com.edubill.edubillApi.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT) //해당객체를 json으로 직렬화
public enum ErrorCode {

    // 프론트에 보여지는 값
    LOGIN_INPUT_INVALID(400, "M002","아이디나 비밀번호가 일치하지 않습니다.");

    private int status;
    private final String code;
    private final String message;
}
