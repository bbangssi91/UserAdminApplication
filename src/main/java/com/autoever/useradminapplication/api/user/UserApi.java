package com.autoever.useradminapplication.api.user;

import com.autoever.useradminapplication.dto.request.LoginRequestDto;
import com.autoever.useradminapplication.dto.request.SignUpRequestDto;
import com.autoever.useradminapplication.dto.response.LoginResponseDto;
import com.autoever.useradminapplication.dto.response.SignUpResponseDto;
import com.autoever.useradminapplication.dto.response.UserSearchResponseDto;
import com.autoever.useradminapplication.global.GlobalApiResponse;
import com.autoever.useradminapplication.service.users.UserLoginService;
import com.autoever.useradminapplication.service.users.facade.UserFacadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserApi {

    private final UserLoginService loginService;
    private final UserFacadeService userFacadeService;

    @PostMapping(value = "/sign-up", produces = "application/json", consumes = "application/json")
    public ResponseEntity<GlobalApiResponse<SignUpResponseDto>> signUp(@RequestBody @Valid SignUpRequestDto request) {
        SignUpResponseDto signUpResponseDto = userFacadeService.signUp(request);
        return ResponseEntity.ok(GlobalApiResponse.success(signUpResponseDto));
    }

    @GetMapping(value = "/users/{id}", produces = "application/json")
    @PreAuthorize("@authChecker.checkUserAccess(#id)")
    public ResponseEntity<GlobalApiResponse<UserSearchResponseDto>> findUserById(@PathVariable Long id) {
        UserSearchResponseDto userById = userFacadeService.findUserById(id);
        return ResponseEntity.ok(GlobalApiResponse.success(userById));
    }

    @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
    public ResponseEntity<GlobalApiResponse<LoginResponseDto>> signUp(@RequestBody @Valid LoginRequestDto request) {

        // 로그인 서비스 호출
        LoginResponseDto response = loginService.login(request);

        //SignUpResponseDto signUpResponseDto = userFacadeService.signUp(request);
        return ResponseEntity.ok(GlobalApiResponse.success(response));
    }

}
