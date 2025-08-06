package com.autoever.useradminapplication.api.user;

import com.autoever.useradminapplication.dto.request.LoginRequestDto;
import com.autoever.useradminapplication.dto.request.SignUpRequestDto;
import com.autoever.useradminapplication.dto.response.LoginResponseDto;
import com.autoever.useradminapplication.dto.response.SignUpResponseDto;
import com.autoever.useradminapplication.dto.response.UserSearchResponseDto;
import com.autoever.useradminapplication.exception.LoginFailedException;
import com.autoever.useradminapplication.global.error.ErrorCode;
import com.autoever.useradminapplication.service.users.UserLoginService;
import com.autoever.useradminapplication.service.users.facade.UserFacadeService;
import com.autoever.useradminapplication.utils.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserApi.class)
public class UserApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserLoginService loginService;

    @MockitoBean
    private UserFacadeService userFacadeService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @DisplayName("사용자 회원가입 정상테스트")
    @Test
    public void 사용자_회원가입_정상테스트() throws Exception {
        // 요청 given
        SignUpRequestDto request = new SignUpRequestDto(
                "user01"
                , "password01"
                , "유저01"
                , "000000-0000000"
                , "010-0000-0000"
                , "서울특별시"
                , "송파구");
        // 응답 given
        SignUpResponseDto response = new SignUpResponseDto(
                 3L
                ,"user01"
                , "유저01"
                , "010-0000-0000"
                ,"서울특별시"
                , "송파구");

        // 회원가입을 하였을 때 정상 처리
        when(userFacadeService.signUp(request)).thenReturn(response);

        mockMvc.perform(post("/api/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userName").value("유저01"));

        verify(userFacadeService).signUp(request);
    }

    @DisplayName("사용자 회원가입시, 필수값 누락시 400 에러")
    @Test
    public void 사용자_회원가입_필수값_검증() throws Exception {

        // 요청 given (필수값을 입력하지 않은 상태에서 회원가입)
        SignUpRequestDto request = new SignUpRequestDto(
                "user01"
                , null
                , null
                , "000000-0000000"
                , "010-0000-0000"
                , "서울특별시"
                , "송파구");

        // when
        mockMvc.perform(post("/api/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andDo(print());

        verify(userFacadeService, times(0)).signUp(request);
    }


    @DisplayName("로그인을 성공하면 JWT access Token 을 발급한다")
    @Test
    void 로그인을_성공하면_JWT_access_토큰을_발급한다() throws Exception {
        LoginRequestDto request = new LoginRequestDto("testUser", "password123");

        LoginResponseDto response = new LoginResponseDto("testUser","test-jwt-token");

        given(loginService.login(any(LoginRequestDto.class)))
                .willReturn(response);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("test-jwt-token"));
    }

    @DisplayName("로그인을 실패하면, 401 에러와 로그인처리를 실패한다")
    @Test
    void 로그인_실패시_401에러_로그인처리_실패() throws Exception {
        LoginRequestDto request = new LoginRequestDto("testUser", "password123");

        when(loginService.login(any(LoginRequestDto.class)))
                .thenThrow(new LoginFailedException(ErrorCode.UNAUTHORIZED, "로그인 실패"));

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        // then : LoginFailedException 이 발생한다
        assertThatThrownBy(() -> loginService.login(request))
                .isInstanceOf(LoginFailedException.class);
    }

    @DisplayName("사용자 조회 성공")
    @Test
    void 사용자_조회_성공() throws Exception {
        Long userId = 3L;

        UserSearchResponseDto response = new UserSearchResponseDto(userId, "hong123", "홍길동", "서울특별시");

        given(userFacadeService.findUserById(userId))
                .willReturn(response);

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accountId").value(response.accountId()))
                .andExpect(jsonPath("$.data.userName").value(response.userName()))
                .andExpect(jsonPath("$.data.city").value(response.city()))
                .andDo(print());
    }


    @DisplayName("요청 Content-type이 JSON이 아니면, 415 예외를 던진다")
    @Test
    void 요청_CONTENT_TYPE_JSON_검증() throws Exception {
        // 요청 given
        SignUpRequestDto request = new SignUpRequestDto(
                "user01"
                , "password01"
                , "유저01"
                , "000000-0000000"
                , "010-0000-0000"
                , "서울특별시"
                , "송파구");

        mockMvc.perform(post("/api/sign-up")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(result -> assertInstanceOf(HttpMediaTypeNotSupportedException.class, result.getResolvedException()))
                .andDo(print());
    }

    @DisplayName("지원하지 않는 메서드로 요청할 경우, METHOD_NOT_ALLOWED 예외")
    @Test
    void 미지원_HTTP_METHOD_요청_검증() throws Exception {
        // 요청 given
        SignUpRequestDto request = new SignUpRequestDto(
                "user01"
                , "password01"
                , "유저01"
                , "000000-0000000"
                , "010-0000-0000"
                , "서울특별시"
                , "송파구");

        mockMvc.perform(get("/api/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertInstanceOf(HttpRequestMethodNotSupportedException.class, result.getResolvedException()))
                .andDo(print());
    }
}
