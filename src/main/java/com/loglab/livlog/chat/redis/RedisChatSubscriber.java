package com.loglab.livlog.chat.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loglab.livlog.chat.dto.response.ChatPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisChatSubscriber {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void onMessage(String message, String channel) {
        try {
            ChatPayload payload = objectMapper.readValue(message, ChatPayload.class);
            String dest = "/topic/chat.room." + payload.getRoomId();
            messagingTemplate.convertAndSend(dest, payload);
            log.info("Broadcasted to room {}: {}", payload.getRoomId(), payload.getText());
        } catch (Exception e) {
            log.error("Redis subscriber error", e);
        }
    }
}