package com.autoever.useradminapplication.service.users;

import com.autoever.useradminapplication.domain.entity.Users;
import com.autoever.useradminapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional(rollbackFor = RuntimeException.class)
    public Users registerUser(Users entity) {
        return userRepository.save(entity);
    }
}
