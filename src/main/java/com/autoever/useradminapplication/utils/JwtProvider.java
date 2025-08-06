package com.autoever.useradminapplication.utils;

import com.autoever.useradminapplication.domain.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private final String secretKey = "67Kg7J207IqkNjTroZzsl5TsvZTrlKnsnbTrkJjslrTshJzstqnrtoTtnojquLTrrLjsnpDqsIDrgpjsmYDrnbw=";
    private static long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간 (밀리초 단위)

    public String createToken(Users currentUser) {
        Claims claims = Jwts.claims()
                .setSubject(currentUser.getAccountId());
        claims.put("id", currentUser.getId());
        claims.put("role", currentUser.getRoleType());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String resolveToken(HttpServletRequest request) {
        // 요청헤더의 인증정보를 가져옴
        String bearerToken = request.getHeader("Authorization");

        // 토큰이 없거나 Bearer타입이 아니면 인증 실패
//        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
//            throw new UnAuthorizedUserException(ErrorCode.UNAUTHORIZED, "Unauthorized: Login required");
//        }

        return (bearerToken != null && bearerToken.startsWith("Bearer "))
                ? bearerToken.substring(7)
                : null;
    }
}
