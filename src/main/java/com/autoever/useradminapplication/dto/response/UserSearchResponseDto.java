package com.autoever.useradminapplication.dto.response;

import com.autoever.useradminapplication.domain.entity.Users;
import lombok.Builder;

@Builder
public record UserSearchResponseDto(
        Long id,
        String accountId,
        String userName,
        String address
) {
        public static UserSearchResponseDto toResponse(Users user) {
                return UserSearchResponseDto.builder()
                        .id(user.getId())
                        .accountId(user.getAccountId())
                        .userName(user.getUserName())
                        .address(user.getAddress())
                        .build();
        }
}