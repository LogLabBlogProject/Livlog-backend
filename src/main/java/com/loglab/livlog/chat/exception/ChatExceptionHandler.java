package com.loglab.livlog.chat.exception;

import com.loglab.livlog.global.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(basePackages = "com.loglab.livlog.chat")
public class ChatExceptionHandler {

    // === 채팅 특화 예외 처리 ===
    
    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<CommonResponse<?>> handleChatRoomNotFound(ChatRoomNotFoundException e) {
        log.warn("채팅방을 찾을 수 없음: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonResponse.error(801, e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CommonResponse<?>> handleUserNotFound(UserNotFoundException e) {
        log.warn("사용자를 찾을 수 없음: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonResponse.error(801, e.getMessage()));
    }

    @ExceptionHandler(DuplicateParticipantException.class)
    public ResponseEntity<CommonResponse<?>> handleDuplicateParticipant(DuplicateParticipantException e) {
        log.warn("중복 참여자: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(CommonResponse.error(803, e.getMessage()));
    }

    @ExceptionHandler(ChatRoomAccessDeniedException.class)
    public ResponseEntity<CommonResponse<?>> handleChatRoomAccessDenied(ChatRoomAccessDeniedException e) {
        log.warn("채팅방 접근 권한 없음: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(CommonResponse.error(804, e.getMessage()));
    }

    @ExceptionHandler(ChatRoomInactiveException.class)
    public ResponseEntity<CommonResponse<?>> handleChatRoomInactive(ChatRoomInactiveException e) {
        log.warn("비활성화된 채팅방: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.GONE)
                .body(CommonResponse.error(805, e.getMessage()));
    }

    @ExceptionHandler(ChatParticipantLimitExceededException.class)
    public ResponseEntity<CommonResponse<?>> handleParticipantLimitExceeded(ChatParticipantLimitExceededException e) {
        log.warn("참여자 수 제한 초과: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.error(806, e.getMessage()));
    }

    @ExceptionHandler(ChatMessageSendFailedException.class)
    public ResponseEntity<CommonResponse<?>> handleMessageSendFailed(ChatMessageSendFailedException e) {
        log.warn("메시지 전송 실패: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(CommonResponse.error(807, e.getMessage()));
    }

    @ExceptionHandler(WebSocketConnectionException.class)
    public ResponseEntity<CommonResponse<?>> handleWebSocketConnection(WebSocketConnectionException e) {
        log.warn("WebSocket 연결 실패: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(CommonResponse.error(808, e.getMessage()));
    }

    // === 입력값 검증 예외 (글로벌과 중복되지만 채팅 특화 메시지) ===
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<?>> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        log.warn("채팅 입력값 검증 실패: {}", errorMessage);
        return ResponseEntity.badRequest()
                .body(CommonResponse.error(802, "입력값 검증 실패: " + errorMessage));
    }

    // === 채팅 특화 IllegalArgumentException ===
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<?>> handleIllegalArgument(IllegalArgumentException e) {
        // 채팅 관련 IllegalArgumentException만 처리 (글로벌과 중복 방지)
        if (e.getMessage().contains("채팅") || e.getMessage().contains("방") || 
            e.getMessage().contains("참여") || e.getMessage().contains("사용자")) {
            log.warn("채팅 관련 잘못된 인수: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(CommonResponse.error(400, e.getMessage()));
        }
        
        // 채팅 관련이 아닌 경우 글로벌 핸들러로 위임
        throw e;
    }

    // === 채팅 특화 예외만 처리하고 나머지는 글로벌로 위임 ===
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<?>> handleGenericException(Exception e) {
        // 채팅 관련 예외가 아닌 경우 글로벌 핸들러로 위임
        if (!isChatRelatedException(e)) {
            throw new RuntimeException(e);
        }
        
        log.error("채팅 관련 예상치 못한 오류 발생", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.error(500, "채팅 서비스 내부 오류가 발생했습니다."));
    }
    
    private boolean isChatRelatedException(Exception e) {
        return e.getMessage() != null && (
            e.getMessage().contains("채팅") || 
            e.getMessage().contains("방") || 
            e.getMessage().contains("참여") ||
            e.getMessage().contains("메시지")
        );
    }
}
