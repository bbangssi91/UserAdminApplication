package com.autoever.useradminapplication.dto.response.admin;

import com.autoever.useradminapplication.constants.enums.RoleType;
import com.autoever.useradminapplication.domain.entity.Users;
import lombok.Builder;

@Builder
public record AdminUserSearchResponseDto(Long id,
                                         String userName,
                                         String accountId,
                                         String phoneNumber,
                                         RoleType roleType
) {
    // Users 엔티티 -> UserSearchResponseDto 변환
    public static AdminUserSearchResponseDto fromEntity(Users user) {
        return AdminUserSearchResponseDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .accountId(user.getAccountId())
                .phoneNumber(user.getPhoneNumber())
                .roleType(user.getRoleType()) // Enum 타입 RoleType
                .build();
    }
}
