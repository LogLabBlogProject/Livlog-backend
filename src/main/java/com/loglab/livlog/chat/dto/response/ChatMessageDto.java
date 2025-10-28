package com.loglab.livlog.chat.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ChatMessageDto {
    private Long messageId;
    private Long roomId;
    private Long senderId;
    private String senderName;
    private String text;
    private Long createdAtEpochMs;
    private boolean mine;           // 요청자 기준
    private Integer readCount;      // 읽은 인원 수 (옵션)
}