package com.autoever.useradminapplication.domain.entity;

import com.autoever.useradminapplication.constants.enums.RoleType;
import com.autoever.useradminapplication.domain.vo.UserVO;
import com.autoever.useradminapplication.global.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID

    private String accountId; // 계정

    private String password; // 암호

    private String userName; // 성명

    private String residentRegistrationNumber; // 주민등록번호

    private String phoneNumber; // 핸드폰번호

    private String address; // 주소

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public UserVO convertToVO(Users users) {
        return UserVO.builder()
                .userName(users.getUserName())
                .address(users.getAddress())
                .phoneNumber(users.getPhoneNumber())
                .residentRegistrationNumber(users.getResidentRegistrationNumber())
                .build();
    }
}