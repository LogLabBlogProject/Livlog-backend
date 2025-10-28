package com.loglab.livlog.chat.controller;

import com.loglab.livlog.chat.dto.request.ChatSendRequestDto;
import com.loglab.livlog.chat.entity.ChatMessage;
import com.loglab.livlog.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
public class ChatStompController {

    private final ChatService chatService;

    // 클라이언트 발행: /app/chat.send.{roomId}
    @MessageMapping("/chat.send.{roomId}")
    public void sendToRoom(@DestinationVariable Long roomId, ChatSendRequestDto req, Principal principal) {
        Long senderId = Long.parseLong(principal.getName());

        // 권한 체크 (room 참여자 여부)
        chatService.assertParticipant(senderId, roomId);

        // 1) DB 저장
        ChatMessage saved = chatService.saveMessage(roomId, senderId, req.getText());

        // 2) Redis publish (모든 인스턴스 팬아웃)
        chatService.publishToRedis(saved);
    }
}