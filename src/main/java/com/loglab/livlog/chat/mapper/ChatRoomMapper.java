package com.loglab.livlog.chat.mapper;

import com.loglab.livlog.chat.dto.response.ChatParticipantDto;
import com.loglab.livlog.chat.dto.response.ChatRoomDto;
import com.loglab.livlog.chat.entity.ChatParticipant;
import com.loglab.livlog.chat.entity.ChatRoom;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatRoomMapper {
    
    /**
     * ChatRoom 엔티티를 ChatRoomDto로 변환
     */
    public ChatRoomDto toDto(ChatRoom room) {
        List<ChatParticipantDto> participants = room.getParticipants().stream()
                .map(this::toParticipantDto)
                .collect(Collectors.toList());

        return ChatRoomDto.builder()
                .roomId(room.getId())
                .title(room.getTitle())
                .type(room.getType().name())
                .createdAtEpochMs(room.getCreatedAt().toEpochMilli())
                .participants(participants)
                .build();
    }
    
    /**
     * ChatParticipant 엔티티를 ChatParticipantDto로 변환
     */
    public ChatParticipantDto toParticipantDto(ChatParticipant participant) {
        return ChatParticipantDto.builder()
                .memberId(participant.getMemberId())
                .username(participant.getMemberName())
                .joinedAtEpochMs(participant.getJoinedAt().toEpochMilli())
                .build();
    }
}
