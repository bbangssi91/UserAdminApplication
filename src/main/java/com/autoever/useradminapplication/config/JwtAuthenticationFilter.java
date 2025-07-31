package com.autoever.useradminapplication.config;

import com.autoever.useradminapplication.utils.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

/**
 *  Authorization: Bearer <JWT> 헤더를 통해 사용자 정보를 파싱하고 인증하는 역할을 수행
 *
 *  - JWT 토큰을 Authorization 헤더에서 추출하고
 *  - 토큰 유효성 검사 후 SecurityContextHolder에 인증 정보 저장
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORITY_PREFIX = "ROLE_";
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // AuthType을 제외한 Header.Payload.signature 를 추출
        String token = jwtProvider.resolveToken(request);

        // 토큰이 있거나, 유효한 토큰일때만 검증
        if (token != null && jwtProvider.validateToken(token)) {

            Claims claims = jwtProvider.getClaims(token);
            Long id = claims.get("id", Long.class);
            String accountId = claims.getSubject();
            String role = claims.get("role", String.class);

            //UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // GrantedAuthority 생성 (role을 기반으로)
            Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(AUTHORITY_PREFIX + role);

            // UserDetails로 직접 생성한 인증 객체 기록
            JwtAuthenticationToken auth =
                    new JwtAuthenticationToken(id, accountId, role, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}