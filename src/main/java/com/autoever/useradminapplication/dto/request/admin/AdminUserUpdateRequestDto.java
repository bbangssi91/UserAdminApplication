package com.autoever.useradminapplication.dto.request.admin;

public record AdminUserUpdateRequestDto(
    String password,
    String address
) {
}