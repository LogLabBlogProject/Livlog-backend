package com.loglab.livlog.auth.smtp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 이메일 인증 코드 전송 요청 DTO
 *
 * @author : 000flag
 * @fileName : EmailAuthRequestDto
 * @since : 2025.10.19
 */
@Getter
public class EmailAuthRequestDto {

    @Schema(description = "사용자 이메일 주소", example = "user@example.com")
    public String receiver;
}
