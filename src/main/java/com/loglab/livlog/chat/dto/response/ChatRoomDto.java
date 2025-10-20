package com.loglab.livlog.chat.dto.response;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ChatRoomDto {

    private Long roomId;
    private String title;
    private String type; // "PRIVATE" | "GROUP"
    private Long createdAtEpochMs;

    // 추가 정보
    private String lastMessage;
    private Long lastMessageAtEpochMs;
    private Integer unreadCount;

    private List<ChatParticipantDto> participants;
}
