package com.loglab.livlog.chat.exception;

public class DuplicateParticipantException extends RuntimeException {
    public DuplicateParticipantException(String message) {
        super(message);
    }
    
    public DuplicateParticipantException(Long roomId, Long memberId) {
        super("사용자가 이미 채팅방에 참여하고 있습니다. 채팅방 ID: " + roomId + ", 사용자 ID: " + memberId);
    }
}
