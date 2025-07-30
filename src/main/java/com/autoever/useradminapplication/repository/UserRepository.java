package com.autoever.useradminapplication.repository;

import com.autoever.useradminapplication.domain.entity.Users;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findUsersByAccountId(@NotBlank(message = "사용자 ID는 필수입니다.") String userId);

    @Query("SELECT u FROM Users u " +
            "WHERE (:userName IS NULL OR u.userName LIKE %:userName%) ")
    Page<Users> findAllByConditions(String userName, Pageable pageable);

}
