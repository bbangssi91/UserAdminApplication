package com.autoever.useradminapplication.service.users.facade;

import com.autoever.useradminapplication.domain.entity.Users;
import com.autoever.useradminapplication.dto.request.SignUpRequestDto;
import com.autoever.useradminapplication.dto.response.SignUpResponseDto;
import com.autoever.useradminapplication.dto.response.UserSearchResponseDto;
import com.autoever.useradminapplication.exception.DataNotFoundException;
import com.autoever.useradminapplication.exception.UniqueViolationException;
import com.autoever.useradminapplication.global.error.ErrorCode;
import com.autoever.useradminapplication.service.users.UserSearchService;
import com.autoever.useradminapplication.service.users.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserFacadeService {

    private final UserSearchService userSearchService;
    private final UserService userService;

    /**
     *  사용자의 회원가입을 수행하는 메서드
     *
     * @param record
     * @return
     */
    public SignUpResponseDto signUp(@Valid SignUpRequestDto record) {

        // 1. 사용자 ID가 중복되는지 검사
        userSearchService.findUsersByAccountId(record.accountId())
                .ifPresent(user -> {
                    throw new UniqueViolationException(ErrorCode.EXISTS_UNIQUE_VALUE, "[" + record.accountId() + "] 이미 존재하는 ID입니다");
                });

        // 2. 유저 회원가입
        Users results = userService.registerUser(record.toEntity());
        return SignUpResponseDto.toResponse(results);
    }

    public UserSearchResponseDto findUserById(Long id) {
        Users user = userSearchService.findUserById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.DATA_NOT_FOUND, "존재하지 않는 사용자입니다"));

        return UserSearchResponseDto.toResponse(user);
    }
}
