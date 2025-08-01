package com.autoever.useradminapplication.service.admin;

import com.autoever.useradminapplication.domain.entity.Users;
import com.autoever.useradminapplication.domain.vo.UserVO;
import com.autoever.useradminapplication.dto.request.admin.AdminUserUpdateRequestDto;
import com.autoever.useradminapplication.dto.response.admin.AdminUserUpdateResponseDto;
import com.autoever.useradminapplication.exception.DataNotFoundException;
import com.autoever.useradminapplication.global.error.ErrorCode;
import com.autoever.useradminapplication.repository.UserRepository;
import com.autoever.useradminapplication.utils.EncryptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminUserService {

    private final EncryptionService passwordEncryptionService;
    private final UserRepository userRepository;

    @Transactional(rollbackFor = RuntimeException.class)
    public AdminUserUpdateResponseDto updateUserInfo(Long id, @Valid AdminUserUpdateRequestDto record) {
        Users users = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.DATA_NOT_FOUND, "존재하지 않는 사용자입니다."));

        UserVO before = users.convertToVO(users);

        // 비밀번호 암호화
        String encodedPasword = passwordEncryptionService.encryptPassword(record.password());

        // 유저정보 갱신
        users.changeUserInfo(encodedPasword, record);
        UserVO after = users.convertToVO(users);

        return AdminUserUpdateResponseDto.toResponse(users.getId(), before, after);
    }
}
