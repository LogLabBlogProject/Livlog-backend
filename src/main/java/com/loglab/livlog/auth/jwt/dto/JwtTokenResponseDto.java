package com.loglab.livlog.auth.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenResponseDto {
    @Builder.Default
    private String tokenType = "Bearer";
    private String accessToken;
    private String refreshToken;
}
