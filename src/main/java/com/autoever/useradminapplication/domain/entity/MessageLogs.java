package com.autoever.useradminapplication.domain.entity;

import com.autoever.useradminapplication.constants.enums.TransmissionStatus;
import com.autoever.useradminapplication.global.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "MESSAGE_LOGS")
@Entity
public class MessageLogs extends BaseEntity {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID

    @Column(nullable = false)
    private String accountId; // 발송 대상자 계정

    @Column(nullable = false)
    private String phone; // 전화번호

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private TransmissionStatus transmissionStatus;

    private Long retryCount; // 재시도횟수

    public static List<MessageLogs> toEntity(List<Users> users, String messageContent) {
        return users.stream()
                .map(user -> MessageLogs.builder()
                        .accountId(user.getAccountId())                        // 사용자 ID
                        .phone(user.getPhoneNumber())                          // 사용자 전화번호
                        .content(createContent(user, messageContent))          // 전달받은 메시지 내용
                        .transmissionStatus(TransmissionStatus.PENDING)        // 초기 전송 상태는 항상 PENDING
                        .retryCount(0L)                                        // 초기 시도 횟수는 0
                        .build())
                .collect(Collectors.toList());
    }

    //{회원 성명}님, 안녕하세요. 현대 오토에버입니다.
    private static String createContent(Users user, String messageContent) {
        StringBuilder sb = new StringBuilder();
        String header = String.format("%s님, 안녕하세요. 현대오토에버입니다. \n", user.getUserName());
        sb.append(header);
        sb.append(messageContent);
        return sb.toString();
    }

}
