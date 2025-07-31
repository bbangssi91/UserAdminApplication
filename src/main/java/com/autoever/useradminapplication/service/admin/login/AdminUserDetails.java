package com.autoever.useradminapplication.service.admin.login;

import com.autoever.useradminapplication.domain.entity.Users;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AdminUserDetails implements UserDetails {

    private final Users users;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security는 내부적으로 hasRole("ADMIN") → ROLE_ADMIN 으로 지정
        return List.of(new SimpleGrantedAuthority("ROLE_" + users.getRoleType().name()));
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getAccountId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
