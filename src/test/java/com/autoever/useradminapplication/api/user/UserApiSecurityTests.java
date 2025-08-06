package com.autoever.useradminapplication.api.user;

import com.autoever.useradminapplication.config.AuthChecker;
import com.autoever.useradminapplication.config.JwtAuthenticationFilter;
import com.autoever.useradminapplication.config.JwtAuthenticationToken;
import com.autoever.useradminapplication.constants.enums.RoleType;
import com.autoever.useradminapplication.domain.entity.Users;
import com.autoever.useradminapplication.service.users.UserLoginService;
import com.autoever.useradminapplication.service.users.facade.UserFacadeService;
import com.autoever.useradminapplication.utils.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import(JwtProvider.class)
@WebMvcTest(UserApi.class)
class UserApiSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider; // 실제 JwtProvider 사용

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserLoginService loginService;

    @MockitoBean
    private UserFacadeService userFacadeService;

    /*********************************************************
     *
     *
     *********************************************************/
    @MockitoBean
    private AuthChecker authChecker;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @DisplayName("유효한 인증된 JWT토큰으로 인증 검증")
    @Test
    void 유효한_인증된_JWT토큰으로_인증_검증() throws Exception {
        // given
        Long userId = 2L;

        Users users = new Users(
                2L, "user02", "user02", "유저02",
                "000000-1234567", "010-1234-1234",
                "서울", "송파구", RoleType.USER);

        // 로그인이 완료되어 생성된 실제 토큰
        String token = jwtProvider.createToken(users);

        // SecurityContext에 직접 주입
        JwtAuthenticationToken auth =
                new JwtAuthenticationToken(2L, "user02", "USER", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(authChecker.checkUserAccess(2L)).thenReturn(true);

        // when & then
        mockMvc.perform(get("/api/users/{id}", userId)
                        .with(authentication(auth))
                        .header("Autorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

}