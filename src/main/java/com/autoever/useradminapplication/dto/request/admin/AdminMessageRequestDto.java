package com.autoever.useradminapplication.dto.request.admin;

import jakarta.validation.constraints.NotBlank;

public record AdminMessageRequestDto(
        @NotBlank(message = "메시지 내용은 필수 입력값입니다.")
        String message
) {
}