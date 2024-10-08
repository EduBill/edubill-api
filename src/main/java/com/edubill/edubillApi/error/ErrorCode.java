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
    GROUP_NOT_FOUND(400, "A002", "반이 존재하지 않습니다."),

    //Student
    STUDENT_NOT_FOUND(400, "S001", "학생이 존재하지 않습니다."),

    //Payment
    PAYMENT_HISTORY_NOT_FOUND(400, "P001","납부내역이 존재하지 않습니다"),
    PAYMENT_KEY_NOT_ENCRYPTED(500, "P002", "결제 키가 암호화되지 않았습니다"),
    PAID_AMOUNT_TUITION_MISMATCH(400,"P003", "납부금액과 학원비가 일치하지 않습니다."),

    // common
    INTERNAL_SERVER_ERROR(500, "C001", "서버 오류"),
    INVALID_INPUT_VALUE(400, "C002", "잘못된 일력 값을 입력하였습니다."),
    INVALID_TYPE_VALUE(400, "C003", "잘못된 타입을 입력하였습니다."),
    BAD_CREDENTIALS(400, "C005", "bad credentials"),
    BAD_GATEWAY_ERROR(502,"C006", "bad gateway"),
    INVALID_DATETIME_VALUE(400,"C007", "날짜 텍스트를 변환할 수 없습니다.");



    private final int status;
    private final String code;
    private final String message;
}