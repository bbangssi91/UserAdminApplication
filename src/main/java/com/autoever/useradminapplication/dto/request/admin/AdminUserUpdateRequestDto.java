package com.autoever.useradminapplication.dto.request.admin;

public record AdminUserUpdateRequestDto(
    Long id,
    String password,
    String city,
    String address
) {
}