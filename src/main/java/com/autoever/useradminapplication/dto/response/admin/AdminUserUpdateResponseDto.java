package com.autoever.useradminapplication.dto.response.admin;

import com.autoever.useradminapplication.domain.vo.UserVO;
import lombok.Builder;

@Builder
public record AdminUserUpdateResponseDto(Long id,
                                         UserVO before,
                                         UserVO after
) {
    public static AdminUserUpdateResponseDto toResponse(Long id, UserVO before, UserVO after) {
        return AdminUserUpdateResponseDto.builder()
                .id(id)
                .before(before)
                .after(after)
                .build();
    }
}
