package com.autoever.useradminapplication.domain.vo;

import com.autoever.useradminapplication.domain.entity.MessageLogs;
import lombok.Builder;

import java.util.List;

/**
 * 트랜잭션 커밋 후 발행할 사용자 메시지 이벤트
 */
@Builder
public record UserMessageEvent (
        String messageChannel,      // 메시지 채널
        List<MessageLogs> userLogs     // 대상 사용자 목록
) {

    public static UserMessageEvent of(List<MessageLogs> userLogs) {
        return new UserMessageEvent("kakaotalk-message-channel", userLogs);
    }
}
