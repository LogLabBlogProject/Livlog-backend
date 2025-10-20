package com.loglab.livlog.chat.exception;

public class ChatParticipantLimitExceededException extends RuntimeException {
    public ChatParticipantLimitExceededException(String message) {
        super(message);
    }
    
    public ChatParticipantLimitExceededException(Long roomId, int currentCount, int maxLimit) {
        super("채팅방 " + roomId + "의 참여자 수가 제한을 초과했습니다. (현재: " + currentCount + ", 최대: " + maxLimit + ")");
    }
}
