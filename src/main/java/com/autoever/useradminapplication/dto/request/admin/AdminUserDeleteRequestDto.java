package com.autoever.useradminapplication.dto.request.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AdminUserDeleteRequestDto(

        @Min(value = 0, message = "ID 최소값은 0 이상입니다")
        @NotNull(message = "ID는 필수입니다")
        Long id
) {
}