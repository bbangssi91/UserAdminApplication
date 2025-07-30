package com.autoever.useradminapplication.service.users;

import com.autoever.useradminapplication.domain.entity.Users;
import com.autoever.useradminapplication.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSearchService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<Users> findUsersByAccountId(@NotBlank(message = "사용자 ID는 필수입니다.") String userId) {
        return userRepository.findUsersByAccountId(userId);
    }

    @Transactional(readOnly = true)
    public Optional<Users> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Users> findAllByConditions(String userName, Pageable pageable) {
        return userRepository.findAllByConditions(userName, pageable);
    }
}
