package com.loglab.livlog.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatStompController {

    private final ChatService chatService;

    // 클라이언트 발행: /app/chat.send.{roomId}
    @MessageMapping("/chat.send.{roomId}")
    public void sendToRoom(@DestinationVariable Long roomId, ChatSendReq req, Principal principal) {
        Long senderId = (Long)((Authentication) principal).getPrincipal();

        // 권한 체크 (room 참여자 여부)
        chatService.assertParticipant(senderId, roomId);

        // 1) DB 저장
        ChatMessage saved = chatService.saveMessage(roomId, senderId, req.getText());

        // 2) Redis publish (모든 인스턴스 팬아웃)
        chatService.publishToRedis(saved);
    }
}