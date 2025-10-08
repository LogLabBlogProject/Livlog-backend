package com.loglab.livlog.auth.jwt.controller;

import com.loglab.livlog.auth.jwt.JwtTokenProvider;
import com.loglab.livlog.auth.jwt.dto.JwtTokenResponseDto;
import com.loglab.livlog.user.dto.LoginRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Login API", description = "Login API")
public class LoginController {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /** 강제로 하드코딩 로그인 */
    @Operation(
            summary = "Token 빠르게 발급받는 로그인 API",
            description = ""
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        // 하드코딩된 이메일과 role
        String email = request.getEmail();  // 테스트용으로 request 그대로 사용
        String role = "USER";

        // JwtTokenProvider를 직접 사용해서 JWT 생성
        String accessToken = jwtTokenProvider.generateAccessToken(email, role);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email, role);

        // DTO 생성 후 리턴
        JwtTokenResponseDto tokenDto = JwtTokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return ResponseEntity.ok(tokenDto);
    }

}
