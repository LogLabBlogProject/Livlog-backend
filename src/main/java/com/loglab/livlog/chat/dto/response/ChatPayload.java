package com.loglab.livlog.chat.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ChatPayload {
    private Long roomId;
    private Long messageId;
    private Long senderId;
    private String senderName;     // 편의 필드(선택)
    private String text;
    private Long sentAtEpochMs;
    private boolean system;        // 시스템 이벤트(입장/퇴장/읽음 등)

    // 선택: 읽음 이벤트용 필드
    private Long readUpToMessageId;
    private Long readerId;
}