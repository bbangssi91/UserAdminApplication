package com.autoever.useradminapplication.dto.request;

import com.autoever.useradminapplication.domain.entity.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpRequestDto(
    @NotBlank(message = "계정 ID는 필수 입력값입니다.") // 공백 또는 null 허용 불가
    String accountId, // 계정

    @NotBlank(message = "비밀번호는 필수 입력값입니다.") // 공백 또는 null 허용 불가
    String password, // 암호

    @NotBlank(message = "성명은 필수 입력값입니다.") // 공백 또는 null 허용 불가
    String userName, // 성명

    @NotBlank(message = "주민등록번호는 필수 입력값입니다.")
    @Pattern(
        regexp = "\\d{6}-\\d{7}",
        message = "주민등록번호 형식은 '숫자6자리-숫자7자리'여야 합니다."
    )
    String residentRegistrationNumber, // 주민등록번호

    @NotBlank(message = "핸드폰 번호는 필수 입력값입니다.")
    @Pattern(
        regexp = "010-\\d{4}-\\d{4}",
        message = "핸드폰 번호는 010-XXXX-XXXX 형태이어야 합니다."
    )
    String phoneNumber, // 핸드폰번호

    @NotBlank(message = "주소는 필수 입력값입니다.") // 공백 또는 null 허용 불가
    String address // 주소
) {
    public Users toEntity() {
        return Users.builder()
                .accountId(accountId)
                .password(password)
                .userName(userName)
                .residentRegistrationNumber(residentRegistrationNumber)
                .phoneNumber(phoneNumber)
                .address(address)
                .build();
    }
}