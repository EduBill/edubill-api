package com.edubill.edubillApi.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExcelResponseDto {

    private String userId;
    private Boolean isTrue; //엑셀업로드 유무
}
