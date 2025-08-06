package com.autoever.useradminapplication.utils;

import com.autoever.useradminapplication.constants.enums.RoleType;
import com.autoever.useradminapplication.domain.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class JwtProviderTests {

    private final JwtProvider jwtProvider = new JwtProvider();

    // Mock Users 객체
    private Users getMockUser() {
        return new Users(
                15L
                , "user01"
                , "user01"
                , "user01"
                , "000000-1234567"
                , "010-1234-1234"
                , "서울"
                , "송파구"
                , RoleType.USER);
    }

    @DisplayName("유효한 토큰의 Claims를 가져와서 정보를 검증")
    @Test
    void 유효한_토큰_검증_및_claims_가져오기() {
        Users user = getMockUser();
        String token = jwtProvider.createToken(user);

        assertTrue(jwtProvider.validateToken(token));

        Claims claims = jwtProvider.getClaims(token);
        assertEquals("user01", claims.getSubject());
        assertEquals(15L, claims.get("id", Long.class));
        assertEquals("USER", claims.get("role", String.class));
    }

    @DisplayName("유효기간이 만료된 토큰은 유효하지않음")
    @Test
    void 토큰시간이_만료되면_유효하지않은_토큰() throws Exception {
        // 1. Reflection을 통해 JwtProvider 내부 만료 시간 강제로 짧게 설정
        Field EXPIRATION_TIME = JwtProvider.class.getDeclaredField("EXPIRATION_TIME");
        EXPIRATION_TIME.setAccessible(true);
        EXPIRATION_TIME.set(null, 1000L); // 1초뒤 만료

        Users user = getMockUser();
        String token = jwtProvider.createToken(user);

        // 2. 토큰이 만료되도록 1.5초 대기
        Thread.sleep(1500);

        assertFalse(jwtProvider.validateToken(token));

        assertThrows(ExpiredJwtException.class, () -> {
            jwtProvider.getClaims(token);
        });
    }
}
