package com.autoever.useradminapplication.dto.response;

public record LoginResponseDto(
    String accountId, // 계정
    String accessToken // 암호
) {
}