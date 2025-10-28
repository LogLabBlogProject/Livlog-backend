package com.loglab.livlog.chat.exception;

public class ChatRoomInactiveException extends RuntimeException {
    public ChatRoomInactiveException(String message) {
        super(message);
    }
    
    public ChatRoomInactiveException(Long roomId) {
        super("채팅방 " + roomId + "이 비활성화되었습니다.");
    }
}
