package com.autoever.useradminapplication.service.users;

import com.autoever.useradminapplication.domain.entity.Users;
import com.autoever.useradminapplication.dto.request.LoginRequestDto;
import com.autoever.useradminapplication.dto.response.LoginResponseDto;
import com.autoever.useradminapplication.exception.DataNotFoundException;
import com.autoever.useradminapplication.exception.LoginFailedException;
import com.autoever.useradminapplication.global.error.ErrorCode;
import com.autoever.useradminapplication.utils.JwtProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserLoginService {

    private final UserSearchService userSearchService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginResponseDto login(@Valid LoginRequestDto request) {
        Users users = userSearchService.findUsersByAccountId(request.accountId())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.DATA_NOT_FOUND, "존재하지 않는 사용자입니다"));

        if(!passwordEncoder.matches(request.password(), users.getPassword())) {
            throw new LoginFailedException(ErrorCode.UNAUTHORIZED, "계정정보가 일치하지 않습니다");
        }

        // 3. JWT 발급
        String token = jwtProvider.createToken(users);

        return new LoginResponseDto(request.accountId(), token);

    }
}
