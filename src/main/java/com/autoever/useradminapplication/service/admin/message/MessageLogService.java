package com.autoever.useradminapplication.service.admin.message;

import com.autoever.useradminapplication.domain.entity.MessageLogs;
import com.autoever.useradminapplication.domain.entity.Users;
import com.autoever.useradminapplication.domain.vo.UserMessageEvent;
import com.autoever.useradminapplication.repository.MessageLogsResitory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageLogService {

    private final ApplicationEventPublisher eventPublisher;
    private final MessageLogsResitory messageLogRepository;

    /**
     *  * Listener >  UserMessageEventListener.java
     *
     *  @param pagedUsers
     *  @param messageContent
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMessageLogs(List<Users> pagedUsers, String messageContent) {
        List<MessageLogs> allLogs = MessageLogs.toEntity(pagedUsers, messageContent);

        // 1. DB에 이벤트 전송이력을 저장한다
        List<MessageLogs> savedMessageLogs = messageLogRepository.saveAll(allLogs);

        // 2. 발행한 이벤트는 Transaction After commit이 수행되면 동작한다
        eventPublisher.publishEvent(UserMessageEvent.of(savedMessageLogs));
    }
}
