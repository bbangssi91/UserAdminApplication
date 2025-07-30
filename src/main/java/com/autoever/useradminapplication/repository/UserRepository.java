package com.autoever.useradminapplication.repository;

import com.autoever.useradminapplication.domain.entity.Users;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findUsersByAccountId(@NotBlank(message = "사용자 ID는 필수입니다.") String userId);
}
