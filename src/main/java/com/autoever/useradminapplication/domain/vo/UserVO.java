package com.autoever.useradminapplication.domain.vo;

import lombok.Builder;

@Builder
public record UserVO(
        String password, // 패스워드
        String address // 주소
) {
}