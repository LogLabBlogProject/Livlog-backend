package com.loglab.livlog.chat.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ChatParticipantDto {
    private Long memberId;
    private String username;
    private String profileImageUrl; // 선택
    private Long joinedAtEpochMs;
}