package com.loglab.livlog.auth.jwt.dto;

import com.loglab.livlog.auth.jwt.entity.DeviceInfo;
import com.loglab.livlog.auth.jwt.entity.JwtToken;
import com.loglab.livlog.user.entity.User;
import lombok.*;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenUpdateRequestDto {
    private Long id;
    private User user;
    private DeviceInfo deviceInfo;

    @Builder.Default
    private String tokenType = "Bearer";
    private String accessToken;
    private String refreshToken;

    private Instant accessTokenIssuedAt;
    private Instant accessTokenExpiresAt;
    private Instant refreshTokenIssuedAt;
    private Instant refreshTokenExpiresAt;

    public static JwtTokenUpdateRequestDto from(JwtToken jwtToken) {
        return JwtTokenUpdateRequestDto.builder()
                .id(jwtToken.getId())
                .user(jwtToken.getUser())
                .tokenType(jwtToken.getTokenType())
                .deviceInfo(jwtToken.getDeviceInfo())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .accessTokenIssuedAt(jwtToken.getAccessTokenIssuedAt())
                .accessTokenExpiresAt(jwtToken.getAccessTokenExpiresAt())
                .refreshTokenIssuedAt(jwtToken.getRefreshTokenIssuedAt())
                .refreshTokenExpiresAt(jwtToken.getRefreshTokenExpiresAt())
                .build();
    }

    public JwtToken toEntity() {
        return JwtToken.builder()
                .id(this.id)
                .user(this.user)
                .deviceInfo(this.deviceInfo)
                .tokenType(this.tokenType)
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken)
                .accessTokenIssuedAt(this.accessTokenIssuedAt)
                .accessTokenExpiresAt(this.accessTokenExpiresAt)
                .refreshTokenIssuedAt(this.refreshTokenIssuedAt)
                .refreshTokenExpiresAt(this.refreshTokenExpiresAt)
                .build();
    }
}
