package com.autoever.useradminapplication.service.admin.message;

import com.autoever.useradminapplication.domain.entity.Users;
import com.autoever.useradminapplication.dto.request.admin.AdminMessageRequestDto;
import com.autoever.useradminapplication.service.users.UserSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMessageService {

    private final UserSearchService userSearchService;
    private final MessageLogService messageLogService;

    /**
     *  Database 에 정상적으로 커밋이 완료되면, 전체 사용자에게 비동기로 메시지를 발송
     */
    public void sendMessageToAllUsers(AdminMessageRequestDto record) {
        int page = 0;
        int pageSize = 3; // 한 번에 100명씩 처리

        while(true) {
            Pageable pageable = PageRequest.of(page, pageSize);

            Page<Users> pageResult = userSearchService.findAllUsers(pageable);
            List<Users> pagedUsers = pageResult.getContent();

            // 조회된 결과가 없으면 종료
            if (pagedUsers.isEmpty()) {
                log.info("No more users to process.");
                break;
            }

            // RDBMS에 메시지 이력 추가 (100건 단위로 트랜잭션 처리)
            try {
                messageLogService.saveMessageLogs(pagedUsers, record.message());
            } catch (RuntimeException e) {
                log.error("[Error] Chunk 실패 , page : {}, pageSize : {}, errorMessage{}", page, pageSize, e.getMessage());
            }

            // 다음 페이지가 없으면 루프 종료
            if (!pageResult.hasNext()) {
                break;
            }

            page++; // 다음 페이지로 이동
        }
    }
}