package com.autoever.useradminapplication.service.listener;

import com.autoever.useradminapplication.common.redis.RedisPublisher;
import com.autoever.useradminapplication.domain.vo.UserMessageEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserMessageEventListener {

    private final ObjectMapper objectMapper;
    private final RedisPublisher redisPublisher;

    /**
     * 트랜잭션 성공 후 메시지 발행을 처리
     */
    @Async("asyncTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishMessagesAsync(UserMessageEvent event) {
        event.userLogs().forEach(user -> {
            log.info("Published message to user: {}", user.getContent());
            String json;
            try {
                json = objectMapper.writeValueAsString(user);
                redisPublisher.publish(event.messageChannel(), json);
            } catch (JsonProcessingException e) {
                log.error("Error while converting UserLog to JSON: {}", e.getMessage());
            }
        });
    }
}