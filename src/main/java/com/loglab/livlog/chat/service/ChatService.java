package com.loglab.livlog.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loglab.livlog.chat.dto.response.ChatPayload;
import com.loglab.livlog.chat.entity.ChatMessage;
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
    private final StringRedisTemplate redisTemplate;
    private final ChannelTopic chatTopic;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatMessage saveMessage(Long roomId, Long senderId, String senderName, String body) {
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
}