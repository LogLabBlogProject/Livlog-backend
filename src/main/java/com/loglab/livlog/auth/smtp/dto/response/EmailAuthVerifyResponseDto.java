package com.loglab.livlog.auth.smtp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 이메일 인증 결과 응답 DTO
 *
 * @author : 000flag
 * @since : 2025.10.19
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuthVerifyResponseDto {

    @Schema(description = "사용자 이메일 주소", example = "user@example.com")
    private String receiver;

    @Schema(description = "인증 성공 여부", example = "true")
    private boolean verified;

    @Schema(description = "추가 메시지", example = "인증에 성공했습니다.")
    private String message;
}
