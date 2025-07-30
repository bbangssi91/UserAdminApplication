package com.autoever.useradminapplication.dto.response;

import com.autoever.useradminapplication.domain.entity.Users;
import lombok.Builder;

@Builder
public record SignUpResponseDto(
        String accountId, // 계정
        String userName, // 성명
        String phoneNumber, // 핸드폰번호
        String address // 주소
) {
        public static SignUpResponseDto toResponse(Users users) {
                return SignUpResponseDto.builder()
                        .accountId(users.getAccountId())
                        .userName(users.getUserName())
                        .phoneNumber(users.getPhoneNumber())
                        .address(users.getAddress())
                        .build();
        }
}