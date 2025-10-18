package com.loglab.livlog.chat.dto.response;

import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ChatRoomDto {
    private Long roomId;
    private String type;           // "PRIVATE" | "GROUP"
    private String title;          // 1:1이면 상대 이름 or 합성 타이틀
    private Long createdAtEpochMs;

    // 미리보기용
    private String lastMessage;
    private Long lastMessageAtEpochMs;
    private Integer unreadCount;

    private List<ChatParticipantDto> participants;
}