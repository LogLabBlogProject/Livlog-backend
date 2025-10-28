package com.loglab.livlog.chat.exception;

public class ChatRoomAccessDeniedException extends RuntimeException {
    public ChatRoomAccessDeniedException(String message) {
        super(message);
    }
    
    public ChatRoomAccessDeniedException(Long roomId, Long userId) {
        super("사용자 " + userId + "는 채팅방 " + roomId + "에 접근할 권한이 없습니다.");
    }
}
