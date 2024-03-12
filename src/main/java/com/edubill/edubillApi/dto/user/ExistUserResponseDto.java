package com.edubill.edubillApi.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "사용자 존재 유무 응답 dto")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExistUserResponseDto {
    private Boolean existUser;
}
