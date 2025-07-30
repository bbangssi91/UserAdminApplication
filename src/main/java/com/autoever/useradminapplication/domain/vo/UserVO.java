package com.autoever.useradminapplication.domain.vo;

import lombok.Builder;

@Builder
public record UserVO(
        String userName, // 성명
        String address, // 주소
        String phoneNumber, // 핸드폰번호
        String residentRegistrationNumber // 주민등록번호
) {
}