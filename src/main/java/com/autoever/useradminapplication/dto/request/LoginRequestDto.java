package com.autoever.useradminapplication.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
    @NotBlank(message = "계정 ID는 필수 입력값입니다.") // 공백 또는 null 허용 불가
    String accountId, // 계정

    @NotBlank(message = "비밀번호는 필수 입력값입니다.") // 공백 또는 null 허용 불가
    String password // 암호
) {

}