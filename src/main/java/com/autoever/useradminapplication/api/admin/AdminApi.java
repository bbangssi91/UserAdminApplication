package com.autoever.useradminapplication.api.admin;

import com.autoever.useradminapplication.dto.response.admin.AdminUserSearchResponseDto;
import com.autoever.useradminapplication.global.GlobalApiResponse;
import com.autoever.useradminapplication.service.users.facade.UserFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminApi {

    private final UserFacadeService userFacadeService;

    @GetMapping(value = "/users", produces = "application/json")
    public ResponseEntity<GlobalApiResponse<Page<AdminUserSearchResponseDto>>> getUsers(
        @RequestParam(required = false) String userName, // 이름 필터
        @PageableDefault(size = 100, page = 0, sort = "id", direction = Sort.Direction.ASC)
        Pageable pageable // Pagination 기반 파라미터
    ) {
        // 서비스 계층에서 필터 및 페이징 조건에 따라 데이터 조회
        Page<AdminUserSearchResponseDto> users = userFacadeService.getUsers(userName, pageable);
        return ResponseEntity.ok(GlobalApiResponse.success(users));
    }
}