package com.loglab.livlog.chat.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class MyRoomSummaryDto {
    private Long roomId;
    private String title;
    private String type;
    private String lastMessage;
    private Long lastMessageAtEpochMs;
    private Integer unreadCount;
}