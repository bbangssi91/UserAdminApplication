package com.autoever.useradminapplication.service.admin.login;

import com.autoever.useradminapplication.domain.entity.Users;
import com.autoever.useradminapplication.exception.DataNotFoundException;
import com.autoever.useradminapplication.global.error.ErrorCode;
import com.autoever.useradminapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminLoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        Users users = userRepository.findUsersByAccountId(accountId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.DATA_NOT_FOUND, "존재하지 않는 사용자입니다."));

        return new AdminUserDetails(users);
    }
}
