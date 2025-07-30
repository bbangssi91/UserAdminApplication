package com.autoever.useradminapplication.api.user;

import com.autoever.useradminapplication.dto.request.SignUpRequestDto;
import com.autoever.useradminapplication.dto.response.SignUpResponseDto;
import com.autoever.useradminapplication.dto.response.UserSearchResponseDto;
import com.autoever.useradminapplication.global.GlobalApiResponse;
import com.autoever.useradminapplication.service.users.facade.UserFacadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserApi {

    private final UserFacadeService userFacadeService;

    @PostMapping(value = "/sign-up", produces = "application/json", consumes = "application/json")
    public ResponseEntity<GlobalApiResponse<SignUpResponseDto>> signUp(@RequestBody @Valid SignUpRequestDto request) {
        SignUpResponseDto signUpResponseDto = userFacadeService.signUp(request);
        return ResponseEntity.ok(GlobalApiResponse.success(signUpResponseDto));
    }

    @GetMapping(value = "/users/{id}", produces = "application/json")
    public ResponseEntity<GlobalApiResponse<UserSearchResponseDto>> findUserById(@PathVariable Long id) {
        UserSearchResponseDto userById = userFacadeService.findUserById(id);
        return ResponseEntity.ok(GlobalApiResponse.success(userById));
    }

}
