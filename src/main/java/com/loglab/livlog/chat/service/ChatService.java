package com.loglab.livlog.chat.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository messageRepo;
    private final StringRedisTemplate redisTemplate;
    private final ChannelTopic chatTopic;
    private final ObjectMapper om = new ObjectMapper();

    @Transactional
    public ChatMessage saveMessage(Long roomId, Long senderId, String text) {
        ChatMessage m = new ChatMessage();
        m.setRoomId(roomId);
        m.setSenderId(senderId);
        m.setBody(text);
        m.setCreatedAt(Instant.now());
        return messageRepo.save(m);
    }

    public void publishToRedis(ChatMessage m) {
        ChatPayload payload = new ChatPayload(
                m.getRoomId(), m.getId(), m.getSenderId(), m.getBody(), m.getCreatedAt().toEpochMilli(), false
        );
        try {
            redisTemplate.convertAndSend(chatTopic.getTopic(), om.writeValueAsString(payload));
        } catch (JsonProcessingException e) { /* log */ }
    }

    public void assertParticipant(Long userId, Long roomId) {
        // participantRepo.existsByRoomIdAndMemberId(...)
        // throw if not participant
    }
}
