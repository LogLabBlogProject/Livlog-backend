package com.loglab.livlog.chat.controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

public class ChatController {
    @PostMapping("/chat/room/private/create")
    public RoomDto createPrivate(@RequestParam Long otherUserId, Principal p) {
        Long me = (Long)((Authentication)p).getPrincipal();
        return chatRoomService.createOrGetPrivateRoom(me, otherUserId);
    }
}
