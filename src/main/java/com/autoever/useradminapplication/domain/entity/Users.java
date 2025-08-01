package com.autoever.useradminapplication.domain.entity;

import com.autoever.useradminapplication.constants.enums.RoleType;
import com.autoever.useradminapplication.domain.vo.UserVO;
import com.autoever.useradminapplication.dto.request.SignUpRequestDto;
import com.autoever.useradminapplication.dto.request.admin.AdminUserUpdateRequestDto;
import com.autoever.useradminapplication.global.auditing.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
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
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID

    @Column(unique = true, nullable = false)
    private String accountId; // 계정

    @Column(nullable = false)
    private String password; // 암호

    private String userName; // 성명

    @Column(unique = true, nullable = false)
    private String residentRegistrationNumber; // 주민등록번호

    private String phoneNumber; // 핸드폰번호

    private String city;

    private String address; // 주소

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    /**
     *  Entity로 변환하는 생성메서드
     *
     * @param record    : 요청 데이터
     * @param password  : BCrypt로 암호화된 비밀번호
     * @param residentRegistrationNumber : AES로 암호화된 주민등록번호
     * @return
     */
    public static Users toEntity(SignUpRequestDto record, String password, String residentRegistrationNumber) {
        return Users.builder()
                .accountId(record.accountId())
                .password(password) // 암호화된 비밀번호
                .userName(record.userName())
                .residentRegistrationNumber(residentRegistrationNumber) // 암호화된 주민등록번호
                .phoneNumber(record.phoneNumber())
                .city(record.city())
                .address(record.address())
                .roleType(RoleType.USER) // 사용자 회원가입
                .build();
    }

    public UserVO convertToVO(Users users) {
        return UserVO.builder()
                .password(users.getPassword())
                .city(users.getCity())
                .address(users.getAddress())
                .build();
    }

    /**
     *  사용자의 정보를 Update하는 메서드
     *  ( 비밀번호와 주소만 변경 가능 )
     *
     *  @param password
     *  @param request
     */
    public void changeUserInfo(String password, @Valid AdminUserUpdateRequestDto request) {
        if(password != null) {
            if(!password.isEmpty()) {
                this.password = password;
            }
        }

        if(request.city() != null) {
            this.city = request.city();
        }

        if(request.address() != null) {
            this.address = request.address();
        }
    }
}