package com.autoever.useradminapplication.service.admin;

import com.autoever.useradminapplication.constants.enums.RoleType;
import com.autoever.useradminapplication.domain.entity.Users;
import com.autoever.useradminapplication.dto.request.admin.AdminUserUpdateRequestDto;
import com.autoever.useradminapplication.dto.response.admin.AdminUserUpdateResponseDto;
import com.autoever.useradminapplication.exception.DataNotFoundException;
import com.autoever.useradminapplication.global.error.ErrorCode;
import com.autoever.useradminapplication.repository.UserRepository;
import com.autoever.useradminapplication.utils.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminUserServiceTests {

    @InjectMocks
    private AdminUserService adminUserService;

    @Mock
    private EncryptionService passwordEncryptionService;

    @Mock
    private UserRepository userRepository;

    private AdminUserUpdateRequestDto request;
    @BeforeEach
    public void setUpRequest() {
        // given
        Long userId = 3L;
        String rawPassword = "테스트패스워드123";
        String encodedPassword = "암호화완료_비밀번호";
        String city = "서울특별시";
        String address = "강남구";

        request = new AdminUserUpdateRequestDto(
                userId, rawPassword, city, address
        );
    }

    @DisplayName("사용자의 비밀번호와 정보를 변경하면, 비밀번호는 암호화되어 처리된다")
    @Test
    void 사용자의_비밀번호와_정보를_변경하면_비밀번호는_암호화되어_처리된다() {

        // given
        String encodedPassword = "암호화완료_비밀번호";

        // 실제 객체
        Users mockUser = new Users(
                3L, "user01", "테스트비밀번호123", "테스트유저",
                "000000-1234567", "010-1234-1234",
                "부산광역시", "해운대구", RoleType.USER
        );

        when(userRepository.findById(request.id())).thenReturn(Optional.of(mockUser));
        when(passwordEncryptionService.encryptPassword(request.password())).thenReturn(encodedPassword);

        // when
        AdminUserUpdateResponseDto result = adminUserService.updateUserInfo(request);

        // then
        assertEquals(request.city(), result.after().city());
        assertEquals(request.address(), result.after().address());
        assertEquals(encodedPassword, mockUser.getPassword()); // 비밀번호가 암호화로 처리되었는지 확인
    }

    @DisplayName("존재하지_않는_사용자의_정보를_업데이트_할수없다 > DataNotFoundException")
    @Test
    public void 존재하지_않는_사용자의_정보를_업데이트_할수없다() {
        // given
        String encodedPassword = "암호화완료_비밀번호";

        Users mockUser = mock(Users.class);

        // when : 없는 사용자의 정보를 변경하려고 시도
        when(userRepository.findById(request.id())).thenThrow(new DataNotFoundException(ErrorCode.DATA_NOT_FOUND, "사용자 조회실패"));

        // then : DataNotFoundException이 발생한다
        assertThatThrownBy(() -> adminUserService.updateUserInfo(request))
                .isInstanceOf(DataNotFoundException.class);

        // 검증 : 비밀번호 암호화 작업 혹은, JPA 영속 엔티티가 변경되지 않았는지 검증
        verify(passwordEncryptionService, times(0)).encryptPassword(anyString());
        verify(mockUser, times(0)).changeUserInfo(encodedPassword, request);
    }
}
