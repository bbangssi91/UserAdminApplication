package com.autoever.useradminapplication.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthChecker {

    public boolean checkUserAccess(Long requestedId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        JwtAuthenticationToken authToken = (JwtAuthenticationToken) auth;

        // 인증받은 사용자 ID와 요청 ID가 동일한지 비교
        // 같으면 권한이 있는 것이고, 다르면 권한이 없는 것
        return authToken.getId().equals(requestedId);
    }
}
