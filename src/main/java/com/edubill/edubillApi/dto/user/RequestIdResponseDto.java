package com.edubill.edubillApi.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "requestId 응답 dto")
@Getter
@AllArgsConstructor
public class RequestIdResponseDto {
    private String requestId;
}
