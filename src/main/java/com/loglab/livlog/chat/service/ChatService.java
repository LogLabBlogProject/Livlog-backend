package com.loglab.livlog.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loglab.livlog.chat.dto.response.ChatPayload;
import com.loglab.livlog.chat.entity.ChatMessage;
import com.loglab.livlog.chat.exception.ChatRoomAccessDeniedException;
import com.loglab.livlog.chat.repository.ChatParticipantRepository;
import com.loglab.livlog.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository messageRepository;
    private final ChatParticipantRepository participantRepository;
    private final StringRedisTemplate redisTemplate;
    private final ChannelTopic chatTopic;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatMessage saveMessage(Long roomId, Long senderId, String body) {
        // TODO: 실제 구현에서는 UserService를 통해 사용자 이름 조회
        String senderName = "user_" + senderId; // 임시 구현
        
        ChatMessage msg = ChatMessage.builder()
                .roomId(roomId)
                .senderId(senderId)
                .senderName(senderName)
                .body(body)
                .createdAt(Instant.now())
                .build();
        return messageRepository.save(msg);
    }

    public void publishToRedis(ChatMessage saved) {
        ChatPayload payload = ChatPayload.builder()
                .roomId(saved.getRoomId())
                .messageId(saved.getId())
                .senderId(saved.getSenderId())
                .senderName(saved.getSenderName())
                .text(saved.getBody())
                .sentAtEpochMs(saved.getCreatedAt().toEpochMilli())
                .build();

        try {
            String json = objectMapper.writeValueAsString(payload);
            redisTemplate.convertAndSend(chatTopic.getTopic(), json);
            log.info("Published message {} to Redis topic {}", saved.getId(), chatTopic.getTopic());
        } catch (Exception e) {
            log.error("Redis publish error", e);
        }
    }

    /**
     * 사용자가 해당 채팅방의 참여자인지 검증. 아니면 AccessDenied 예외 발생.
     */
    public void assertParticipant(Long memberId, Long roomId) {
        boolean isParticipant = participantRepository.existsByRoomIdAndMemberId(roomId, memberId);
        if (!isParticipant) {
            throw new ChatRoomAccessDeniedException(roomId, memberId);
        }
    }
}