package com.autoever.useradminapplication.service.users.facade;

import com.autoever.useradminapplication.domain.entity.Users;
import com.autoever.useradminapplication.dto.request.SignUpRequestDto;
import com.autoever.useradminapplication.dto.response.SignUpResponseDto;
import com.autoever.useradminapplication.exception.UniqueViolationException;
import com.autoever.useradminapplication.service.users.UserSearchService;
import com.autoever.useradminapplication.service.users.UserService;
import com.autoever.useradminapplication.utils.EncryptionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserFacadeServiceTests {

    @InjectMocks
    private UserFacadeService userFacadeService;

    @Mock
    private UserSearchService userSearchService;

    @Mock
    private UserService userService;

    @Mock
    private EncryptionService encryptionService;

    private SignUpRequestDto request;

    @BeforeEach
    public void setUp() {
        request = new SignUpRequestDto("user01"
                , "user01"
                , "유저01"
                , "000000-0000000"
                , "010-1234-1234"
                , "서울특별시"
                , "송파구");
    }

    @DisplayName("[유저] 회원가입이 정상적으로 통과된다")
    @Test
    public void 회원가입이_정상적으로_통과된다() {

        // 동일한 ID가 없음
        when(userSearchService.findUsersByAccountId(request.accountId()))
                .thenReturn(Optional.empty());

        // 동일한 주민번호가 없음
        when(userSearchService.findByResidentRegistrationNumber(request.residentRegistrationNumber()))
                .thenReturn(Optional.empty());

        // 비밀번호를 BCrypt 기반으로 암호화
        when(encryptionService.encryptPassword(request.password())).thenReturn("encodedPassword");

        // 주민등록번호를 AES 기반으로 암호화
        when(encryptionService.encryptByAES(request.residentRegistrationNumber())).thenReturn("encryptedRRN");

        Users registered = Users.toEntity(request, "encodedPassword", "encryptedRRN");

        when(userService.registerUser(any(Users.class))).thenReturn(registered);

        SignUpResponseDto response = userFacadeService.signUp(request);

        // then
        assertThat(response).isNotNull();
        verify(userSearchService).findUsersByAccountId(request.accountId());
        verify(userSearchService).findByResidentRegistrationNumber(request.residentRegistrationNumber());
        verify(encryptionService).encryptPassword(request.password());
        verify(encryptionService).encryptByAES(request.residentRegistrationNumber());
        verify(userService).registerUser(any(Users.class));
    }

    @DisplayName("동일한 계정으로 회원가입 불가")
    @Test
    public void 동일한_계정으로_회원가입_불가 () {

        // 동일한 계정으로 가입된 회원이 존재
        when(userSearchService.findUsersByAccountId(request.accountId()))
                .thenReturn(Optional.of(mock(Users.class)));

        // when & then
        assertThatThrownBy(() -> userFacadeService.signUp(request))
                .isInstanceOf(UniqueViolationException.class)
                .hasMessageContaining("이미 존재하는 ID입니다");

        verify(userSearchService, times(1)).findUsersByAccountId(request.accountId());
        verify(userSearchService, times(0)).findByResidentRegistrationNumber(request.residentRegistrationNumber());
        verify(encryptionService, times(0)).encryptPassword(request.password());
        verify(encryptionService, times(0)).encryptPassword(request.residentRegistrationNumber());
        verify(userService, times(0)).registerUser(any(Users.class));
    }

    @DisplayName("동일한 주민등록번호로 회원가입 불가")
    @Test
    public void 동일한_주민등록번호_회원가입_불가 () {

        // 동일한 계정으로 가입된 회원이 존재
        when(userSearchService.findUsersByAccountId(request.accountId()))
                .thenReturn(Optional.empty());

        when(userSearchService.findByResidentRegistrationNumber(request.residentRegistrationNumber()))
                .thenReturn(Optional.of(mock(Users.class)));

        // when & then
        assertThatThrownBy(() -> userFacadeService.signUp(request))
                .isInstanceOf(UniqueViolationException.class)
                .hasMessageContaining("이미 존재하는 주민번호입니다");

        verify(userSearchService, times(1)).findUsersByAccountId(request.accountId());
        verify(userSearchService, times(1)).findByResidentRegistrationNumber(request.residentRegistrationNumber());
        verify(encryptionService, times(0)).encryptPassword(request.password());
        verify(encryptionService, times(0)).encryptPassword(request.residentRegistrationNumber());
        verify(userService, times(0)).registerUser(any(Users.class));
    }
}
