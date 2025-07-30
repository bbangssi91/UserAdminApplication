package com.autoever.useradminapplication.dto.request;

import com.autoever.useradminapplication.domain.entity.Users;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequestDto(

        @NotBlank(message = "아이디 필수값")
        String accountId, // 계정
        String password, // 암호
        String userName, // 성명
        String residentRegistrationNumber, // 주민등록번호
        String phoneNumber, // 핸드폰번호
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