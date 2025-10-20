package com.loglab.livlog.chat.exception;

public class WebSocketConnectionException extends RuntimeException {
    public WebSocketConnectionException(String message) {
        super(message);
    }
    
    public WebSocketConnectionException(Long userId, String reason) {
        super("사용자 " + userId + "의 WebSocket 연결에 실패했습니다. 사유: " + reason);
    }
}
