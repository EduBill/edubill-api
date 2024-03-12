package com.edubill.edubillApi.dto.result;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultResponse {

    private Object data;
    private String message;
    private String status;
    private int status_code;

    public static ResultResponse of(ResultCode resultCode, Object data) {
        return new ResultResponse(resultCode, data);
    }

    public ResultResponse(ResultCode resultCode, Object data) {
        this.data = data;
        this.message = resultCode.getMessage();
        this.status = resultCode.getStatus();
        this.status_code = resultCode.getStatus_code();
    }
}