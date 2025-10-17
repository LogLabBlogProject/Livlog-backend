package com.loglab.livlog.chat.controller;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRestController {

    private final ChatQueryService chatQueryService;
    private final ChatReadService chatReadService;

    // 히스토리 조회 (cursor/paging)
    @GetMapping("/history/{roomId}")
    public Page<ChatMessageDto> history(@PathVariable Long roomId,
                                        @RequestParam(required=false) Long beforeMessageId,
                                        @RequestParam(defaultValue = "50") int size,
                                        Principal p) {
        Long userId = (Long)((Authentication)p).getPrincipal();
        chatQueryService.assertParticipant(userId, roomId);
        return chatQueryService.loadHistory(roomId, beforeMessageId, size);
    }

    // 읽음 처리
    @PutMapping("/room/{roomId}/read")
    public void markRead(@PathVariable Long roomId,
                         @RequestParam Long upToMessageId,
                         Principal p) {
        Long userId = (Long)((Authentication)p).getPrincipal();
        chatReadService.markRead(roomId, userId, upToMessageId);

        // (선택) Redis로 읽음 이벤트 브로드캐스트
        // payload.system = true, text = "READ:{userId}:{upToMessageId}"
    }
}

