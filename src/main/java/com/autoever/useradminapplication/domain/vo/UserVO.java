package com.autoever.useradminapplication.domain.vo;

import lombok.Builder;

@Builder
public record UserVO(
        String city, // 주소
        String address
) {
}