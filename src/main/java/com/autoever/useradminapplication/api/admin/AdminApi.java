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

    @PutMapping(value = "/users", produces = "application/json", consumes = "application/json")
    public ResponseEntity<GlobalApiResponse<AdminUserUpdateResponseDto>> updateUserInfo(@RequestBody @Valid AdminUserUpdateRequestDto request) {
        AdminUserUpdateResponseDto userUpdateResponseDto = adminUserService.updateUserInfo(request);
        return ResponseEntity.ok(GlobalApiResponse.success(userUpdateResponseDto));
    }

    @PostMapping(value = "/send-message-all-user", produces = "application/json", consumes = "application/json")
    public void sendMessageToAllUsers(@RequestBody AdminMessageRequestDto request) {
        sendMessageService.sendMessageToAllUsers(request);
    }


}