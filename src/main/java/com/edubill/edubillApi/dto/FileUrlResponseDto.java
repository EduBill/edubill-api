package com.edubill.edubillApi.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileUrlResponseDto {
    private String s3URL;
}
