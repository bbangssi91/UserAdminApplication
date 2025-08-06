package com.autoever.useradminapplication.service.users.facade;

import com.autoever.useradminapplication.domain.entity.Users;
import com.autoever.useradminapplication.dto.request.SignUpRequestDto;
import com.autoever.useradminapplication.dto.response.SignUpResponseDto;
import com.autoever.useradminapplication.dto.response.UserSearchResponseDto;
import com.autoever.useradminapplication.dto.response.admin.AdminUserSearchResponseDto;
import com.autoever.useradminapplication.exception.DataNotFoundException;
import com.autoever.useradminapplication.exception.UniqueViolationException;
import com.autoever.useradminapplication.global.error.ErrorCode;
import com.autoever.useradminapplication.service.users.UserSearchService;
import com.autoever.useradminapplication.service.users.UserService;
import com.autoever.useradminapplication.utils.EncryptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserFacadeService {

    private final UserSearchService userSearchService;
    private final UserService userService;
    private final EncryptionService encryptionService;

    /**
     *  사용자의 회원가입을 수행하는 메서드
     *
     * @param record
     * @return
     */
    public SignUpResponseDto signUp(@Valid SignUpRequestDto record) {

        // 1. 사용자 계정이 중복되는지 검사
        userSearchService.findUsersByAccountId(record.accountId())
                .ifPresent(user -> {
                    throw new UniqueViolationException(ErrorCode.EXISTS_UNIQUE_VALUE, "[" + record.accountId() + "] 이미 존재하는 ID입니다");
                });

        // 2. 사용자 주민번호 중복되는지 검사
        userSearchService.findByResidentRegistrationNumber(record.residentRegistrationNumber())
                .ifPresent(user -> {
                    throw new UniqueViolationException(ErrorCode.EXISTS_UNIQUE_VALUE, "이미 존재하는 주민번호입니다");
                });

        // 3. 비밀번호와 주민등록번호를 암호화
        String encode = encryptionService.encryptPassword(record.password());
        String encrypt = encryptionService.encryptByAES(record.residentRegistrationNumber());
        Users encryptedEntity = Users.toEntity(record, encode, encrypt);

        // 4. 유저 회원가입
        Users results = userService.registerUser(encryptedEntity);

        return SignUpResponseDto.toResponse(results);
    }

    public UserSearchResponseDto findUserById(Long id) {
        Users user = userSearchService.findUserById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.DATA_NOT_FOUND, "존재하지 않는 사용자입니다"));

        return UserSearchResponseDto.toResponse(user);
    }

    public List<AdminUserSearchResponseDto> getUsers(String userName, Pageable pageable) {
        // 필터 및 페이징 조건을 서비스 계층으로 전달
        Page<Users> usersPage = userSearchService.findAllByConditions(userName, pageable);

        // Users 엔티티를 UserSearchResponseDto로 변환
        return usersPage.getContent().stream()
                .map(AdminUserSearchResponseDto::fromEntity)
                .toList();
    }

}