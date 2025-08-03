package com.autoever.useradminapplication.api.admin;

import com.autoever.useradminapplication.dto.request.admin.AdminMessageRequestDto;
import com.autoever.useradminapplication.dto.request.admin.AdminUserUpdateRequestDto;
import com.autoever.useradminapplication.dto.response.admin.AdminUserSearchResponseDto;
import com.autoever.useradminapplication.dto.response.admin.AdminUserUpdateResponseDto;
import com.autoever.useradminapplication.global.GlobalApiResponse;
import com.autoever.useradminapplication.service.admin.AdminUserService;
import com.autoever.useradminapplication.service.admin.message.SendMessageService;
import com.autoever.useradminapplication.service.users.facade.UserFacadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminApi {

    private final UserFacadeService userFacadeService;
    private final AdminUserService adminUserService;
    private final SendMessageService sendMessageService;
    /**
     *  테스트용 관리자 Basic Auth 로그인
     *
     * @param userName
     * @param pageable
     * @return
     */
//    @GetMapping("/login")
//    public ResponseEntity<?> login(Authentication authentication) {
//        // 인증이 성공되었을 경우에만 이 메서드까지 도달함
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        return ResponseEntity.ok(Map.of(
//                "accountId", userDetails.getUsername(),
//                "roleType", userDetails.getAuthorities()
//        ));
//    }

    @GetMapping(value = "/users", produces = "application/json")
    public ResponseEntity<GlobalApiResponse<List<AdminUserSearchResponseDto>>> getUsers(
        @RequestParam(required = false) String userName, // 이름 필터
        @PageableDefault(size = 100, page = 0, sort = "id", direction = Sort.Direction.ASC)
        Pageable pageable // Pagination 기반 파라미터
    ) {
        // 서비스 계층에서 필터 및 페이징 조건에 따라 데이터 조회
        List<AdminUserSearchResponseDto> users = userFacadeService.getUsers(userName, pageable);
        return ResponseEntity.ok(GlobalApiResponse.success(users));
    }

    @PutMapping(value = "/users/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<GlobalApiResponse<AdminUserUpdateResponseDto>> updateUserInfo(@PathVariable Long id, @RequestBody @Valid AdminUserUpdateRequestDto request) {
        AdminUserUpdateResponseDto userUpdateResponseDto = adminUserService.updateUserInfo(id, request);
        return ResponseEntity.ok(GlobalApiResponse.success(userUpdateResponseDto));
    }

    @PostMapping(value = "/send-message-all-user", produces = "application/json", consumes = "application/json")
    public void sendMessageToAllUsers(@RequestBody AdminMessageRequestDto request) {
        sendMessageService.sendMessageToAllUsers(request);
    }


}