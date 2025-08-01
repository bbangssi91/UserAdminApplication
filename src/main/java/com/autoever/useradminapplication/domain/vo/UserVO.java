package com.autoever.useradminapplication.domain.vo;

import lombok.Builder;

@Builder
public record UserVO(
        String password, // 패스워드
        String city, // 주소
        String address
) {
}