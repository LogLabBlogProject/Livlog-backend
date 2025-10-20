package com.loglab.livlog.auth.smtp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 이메일 인증 코드 검증 요청 DTO
 *
 * 사용자가 입력한 이메일 주소와 인증 코드를 전달받습니다.
 *
 * @author : 000flag
 * @since : 2025.10.15
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuthVerifyRequestDto {

    @Schema(description = "사용자 이메일 주소", example = "user@example.com")
    private String receiver;

    @Schema(description = "사용자가 입력한 인증코드 (6자리)", example = "482917")
    private String code;
}