package com.autoever.useradminapplication.service.users;

import com.autoever.useradminapplication.domain.entity.Users;
import com.autoever.useradminapplication.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    public Optional<Users> findByResidentRegistrationNumber(@NotBlank(message = "주민등록번호는 필수 입력값입니다.") @Pattern(
            regexp = "\\d{6}-\\d{7}",
            message = "주민등록번호 형식은 '숫자6자리-숫자7자리'여야 합니다."
    ) String residentId) {
        return userRepository.findByResidentRegistrationNumber(residentId);
    }

    @Transactional(readOnly = true)
    public Optional<Users> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Users> findAllByConditions(String userName, Pageable pageable) {
        return userRepository.findAllByConditions(userName, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Users> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
