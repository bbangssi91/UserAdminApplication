package com.autoever.useradminapplication.config;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Long id;
    private final String accountId;  // JWT에서 추출된 사용자 고유 ID
    private final String role;       // JWT에서 추출된 역할 정보

    public JwtAuthenticationToken(Long id, String accountId, String role, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.id = id;
        this.accountId = accountId;
        this.role = role;
        setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return null; // // JWT로 인증하므로 비밀번호는 없음
    }

    @Override
    public Object getPrincipal() {
        return accountId;
    }
}
