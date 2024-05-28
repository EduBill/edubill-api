package com.edubill.edubillApi.dto.payment;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoResponseDto {
    private String memoDescription;
}
