package com.loglab.livlog.chat.mapper;

import com.loglab.livlog.chat.dto.response.*;
import com.loglab.livlog.chat.entity.ChatMessage;
import com.loglab.livlog.chat.entity.ChatParticipant;
import com.loglab.livlog.chat.entity.ChatRoom;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ChatMapper {

    public ChatMessageDto toMessageDto(ChatMessage m, Long requesterId, int readCount) {
        return ChatMessageDto.builder()
                .messageId(m.getId())
                .roomId(m.getRoomId())
                .senderId(m.getSenderId())
                .senderName(m.getSenderName()) // 엔티티에 없다면 조인 or 별도 조회
                .text(m.getBody())
                .createdAtEpochMs(m.getCreatedAt().toEpochMilli())
                .mine(m.getSenderId().equals(requesterId))
                .readCount(readCount)
                .build();
    }

    public ChatParticipantDto toParticipantDto(ChatParticipant p) {
        return ChatParticipantDto.builder()
                .memberId(p.getMemberId())
                .username(p.getMemberName())
                //.profileImageUrl(p.getProfileImageUrl())
                .joinedAtEpochMs(p.getJoinedAt().toEpochMilli())
                .build();
    }

    public ChatRoomDto toRoomDto(ChatRoom r,
                                 String title,
                                 String lastMessage,
                                 Long lastAt,
                                 int unreadCount,
                                 java.util.List<ChatParticipantDto> participants) {
        return ChatRoomDto.builder()
                .roomId(r.getId())
                .type(r.getType().name())
                .title(title)
                .createdAtEpochMs(r.getCreatedAt().toEpochMilli())
                .lastMessage(lastMessage)
                .lastMessageAtEpochMs(lastAt)
                .unreadCount(unreadCount)
                .participants(participants)
                .build();
    }
}