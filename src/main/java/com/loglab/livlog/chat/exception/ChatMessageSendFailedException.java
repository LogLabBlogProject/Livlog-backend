package com.loglab.livlog.chat.exception;

public class ChatMessageSendFailedException extends RuntimeException {
    public ChatMessageSendFailedException(String message) {
        super(message);
    }
    
    public ChatMessageSendFailedException(Long roomId, String reason) {
        super("채팅방 " + roomId + "에서 메시지 전송에 실패했습니다. 사유: " + reason);
    }
}
