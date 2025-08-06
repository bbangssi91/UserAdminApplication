package com.autoever.useradminapplication.api.admin;

import com.autoever.useradminapplication.constants.enums.RoleType;
import com.autoever.useradminapplication.domain.vo.UserVO;
import com.autoever.useradminapplication.dto.request.admin.AdminMessageRequestDto;
import com.autoever.useradminapplication.dto.request.admin.AdminUserUpdateRequestDto;
import com.autoever.useradminapplication.dto.response.admin.AdminUserSearchResponseDto;
import com.autoever.useradminapplication.dto.response.admin.AdminUserUpdateResponseDto;
import com.autoever.useradminapplication.service.admin.AdminUserService;
import com.autoever.useradminapplication.service.admin.message.SendMessageService;
import com.autoever.useradminapplication.service.users.facade.UserFacadeService;
import com.autoever.useradminapplication.utils.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AdminApi.class)
class AdminApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserFacadeService userFacadeService;

    @MockitoBean
    private AdminUserService adminUserService;

    @MockitoBean
    private SendMessageService sendMessageService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("GET /admin/users - 관리자가_검색조건으로_사용자_목록을_조회한다")
    void 관리자가_검색조건으로_사용자_목록을_조회한다() throws Exception {
        //given
        String userName = "홍길동";
        Pageable pageable = PageRequest.of(0, 100);
        List<AdminUserSearchResponseDto> mockUsers = List.of(
                new AdminUserSearchResponseDto(1L, "홍길동", "gildong1","010-1234-5678", RoleType.USER),
                new AdminUserSearchResponseDto(2L, "홍길동", "mrhong12345","010-9876-5432", RoleType.USER)
        );

        when(userFacadeService.getUsers(eq(userName), any(Pageable.class))).thenReturn(mockUsers);

        // when & then
        mockMvc.perform(get("/admin/users")
                        .param("userName", userName)
                        .param("page", "0")
                        .param("size", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].userName").value("홍길동"))
                .andExpect(jsonPath("$.data[1].userName").value("홍길동"));

        verify(userFacadeService).getUsers(eq(userName), any(Pageable.class));
    }


    @Test
    @DisplayName("PUT /admin/users - 사용자의_주소를_수정하면_주소가_변경된다")
    void 사용자의_주소를_수정하면_주소가_변경된다() throws Exception {
        // given
        Long userId = 1L;
        AdminUserUpdateRequestDto request = new AdminUserUpdateRequestDto(12345L, "010-1234-5678", "서울시", "송파구");
        UserVO before = new UserVO("12345", "서울시", "송파구");
        UserVO after = new UserVO("12345", "서울시", "강동구");
        AdminUserUpdateResponseDto responseDto = new AdminUserUpdateResponseDto(userId, before, after);

        when(adminUserService.updateUserInfo(any(AdminUserUpdateRequestDto.class)))
                .thenReturn(responseDto);

        // when & then
        mockMvc.perform(
                MockMvcRequestBuilders.put("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(userId))
                .andExpect(jsonPath("$.data.before.address").value("송파구"))
                .andExpect(jsonPath("$.data.after.address").value("강동구"));
                //.andExpect(jsonPath("$.data.phoneNumber").value("010-1234-5678"));

        verify(adminUserService).updateUserInfo(any(AdminUserUpdateRequestDto.class));
    }


    @Test
    @DisplayName("PUT /admin/users - 사용자_정보를_수정할때_사용자ID_필수검증")
    void 사용자_정보를_수정할때_사용자ID_필수검증() throws Exception {

        // given
        AdminUserUpdateRequestDto request = new AdminUserUpdateRequestDto(null, "12345", "서울시", "송파구");

        // When
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.put("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        perform.andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andDo(print());


        // Then : 유효성 검증 실패 시 컨트롤러 로직이 실행되지 않고 예외 응답이 반환되는 흐름 검증
        verify(adminUserService, times(0)).updateUserInfo(any(AdminUserUpdateRequestDto.class));

    }

    @Test
    @DisplayName("POST /admin/send-message-all-user - 전체 사용자 메시지 전송")
    void testSendMessageToAllUsers() throws Exception {
        // given
        AdminMessageRequestDto requestDto = new AdminMessageRequestDto("테스트 메시지내용");

        doNothing().when(sendMessageService).sendMessageToAllUsers(any(AdminMessageRequestDto.class));

        // when & then
        mockMvc.perform(post("/admin/send-message-all-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        verify(sendMessageService).sendMessageToAllUsers(any(AdminMessageRequestDto.class));
    }
}
