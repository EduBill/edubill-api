package com.edubill.edubillApi.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record RequestVerficiationNumberDto(
        @NotEmpty
        @Schema(description = "핸드폰 번호", example = "01012345678")
        String phoneNumber
) {
}
