package com.loglab.livlog.auth.smtp.controller;

import com.loglab.livlog.auth.smtp.dto.request.EmailAuthVerifyRequestDto;
import com.loglab.livlog.auth.smtp.dto.response.EmailAuthSendResponseDto;
import com.loglab.livlog.auth.smtp.dto.response.EmailAuthVerifyResponseDto;
import com.loglab.livlog.global.dto.CommonResponse;
import com.loglab.livlog.auth.smtp.dto.request.EmailAuthRequestDto;
import com.loglab.livlog.auth.smtp.service.EmailAuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 이메일 인증 코드 컨트롤러
 *
 * @author : 000flag
 * @fileName : EmailAuthController
 * @since : 2025.10.19
 */
@RestController
@RequestMapping("/api/auth/email")
@RequiredArgsConstructor
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    @Operation(summary = "이메일 인증코드 발송", description = "입력된 이메일 주소로 인증코드를 발송합니다.")
    @PostMapping("/send")
    public ResponseEntity<CommonResponse<EmailAuthSendResponseDto>> sendAuthCode(@RequestBody EmailAuthRequestDto dto) {
        EmailAuthSendResponseDto result = emailAuthService.sendAuthCode(dto);
        return ResponseEntity.ok(CommonResponse.success(result));
    }

    @PostMapping("/verify")
    @Operation(summary = "이메일 인증코드 검증", description = "입력한 이메일과 인증코드를 검증합니다.")
    public ResponseEntity<CommonResponse<EmailAuthVerifyResponseDto>> verifyEmail(
            @RequestBody EmailAuthVerifyRequestDto dto) {

        EmailAuthVerifyResponseDto response = emailAuthService.verifyCode(dto);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

}
