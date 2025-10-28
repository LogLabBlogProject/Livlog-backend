package com.loglab.livlog.auth.smtp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 이메일 인증코드 발송 결과 응답 DTO
 *
 * @author : 000flag
 * @since : 2025.10.19
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuthSendResponseDto {

    @Schema(description = "수신자 이메일 주소", example = "user@example.com")
    private String receiver;

    @Schema(description = "발송 성공 여부", example = "true")
    private boolean success;

    @Schema(description = "응답 메시지", example = "인증코드 발송 완료")
    private String message;
}
