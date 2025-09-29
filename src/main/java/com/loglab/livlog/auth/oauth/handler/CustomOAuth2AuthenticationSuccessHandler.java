package com.loglab.livlog.auth.oauth.handler;

import com.loglab.livlog.auth.jwt.dto.JwtTokenResponseDto;
import com.loglab.livlog.auth.jwt.dto.JwtTokenUpdateRequestDto;
import com.loglab.livlog.auth.jwt.entity.DeviceInfo;
import com.loglab.livlog.auth.jwt.entity.JwtToken;
import com.loglab.livlog.auth.jwt.CustomUserDetails;
import com.loglab.livlog.auth.jwt.JwtTokenProvider;
import com.loglab.livlog.auth.jwt.repository.JwtTokenRepository;
import com.loglab.livlog.global.dto.CommonResponse;
import com.loglab.livlog.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenRepository jwtTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        /* 로그인 성공 처리 로직 */
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        DeviceInfo deviceInfo = getDeviceInfo(request);
        String deviceId = deviceInfo.getDeviceId();
        User user = customUserDetails.getUser();

        /* JWT 토큰 생성 */
        String accessToken = jwtTokenProvider.generateAccessToken(customUserDetails.getEmail(), customUserDetails.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(customUserDetails.getEmail(), customUserDetails.getRole());
        Claims accessTokenClaims = jwtTokenProvider.parseClaims(accessToken);
        Claims refreshTokenClaims = jwtTokenProvider.parseClaims(refreshToken);

        // TODO: 토큰 정보 확인 후 존재하면 갱신, 존재하지 않으면 생성
        JwtToken jwtToken = jwtTokenRepository.findByUserAndDeviceInfo_DeviceId(user, deviceId)
                .map((token) -> token.update(JwtTokenUpdateRequestDto.builder()
                        .deviceInfo(deviceInfo)
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .accessTokenIssuedAt(accessTokenClaims.getIssuedAt().toInstant())
                        .accessTokenExpiresAt(accessTokenClaims.getExpiration().toInstant())
                        .refreshTokenIssuedAt(refreshTokenClaims.getIssuedAt().toInstant())
                        .refreshTokenExpiresAt(refreshTokenClaims.getExpiration().toInstant())
                        .build()))
                .orElseGet(() -> JwtToken.builder()
                        .user(user)
                        .deviceInfo(deviceInfo)
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .accessTokenIssuedAt(accessTokenClaims.getIssuedAt().toInstant())
                        .refreshTokenIssuedAt(refreshTokenClaims.getIssuedAt().toInstant())
                        .accessTokenExpiresAt(accessTokenClaims.getExpiration().toInstant())
                        .refreshTokenExpiresAt(refreshTokenClaims.getExpiration().toInstant())
                        .build());

        /* TODO: JWT 토큰 및 디바이스 정보 저장 */
        printLog(jwtToken, deviceInfo);
        jwtTokenRepository.save(jwtToken);
        CommonResponse<?> successResponse = CommonResponse.success(
                JwtTokenResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build()
        );

        // TODO: 추후 HttpOnly 쿠키 + CSRF 보완 필요
        // NOTE: OAuth2 Google 테스트 URL: http://localhost:8080/oauth2/authorization/google */
        String json = new ObjectMapper().writeValueAsString(successResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(json);
        // response.sendRedirect("/");
    }

    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private DeviceInfo getDeviceInfo(HttpServletRequest request) {
        return DeviceInfo.builder()
                .deviceId(getDeviceId(request))
                .deviceType(getDeviceType(request))
                .deviceName(getDeviceName(request))
                .ipAddress(getIpAddress(request))
                .userAgent(request.getHeader(HttpHeaders.USER_AGENT))
                .build();
    }

    private String getDeviceId(HttpServletRequest request) {
        String deviceId = request.getHeader("X-Device-ID");
        return deviceId != null ? deviceId : generateDeviceId(request);
    }

    private DeviceInfo.DeviceType getDeviceType(HttpServletRequest request) {
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT).toLowerCase();
        return !userAgent.contains("mobile") ? DeviceInfo.DeviceType.WEB :
                userAgent.contains("android") ? DeviceInfo.DeviceType.ANDROID :
                        DeviceInfo.DeviceType.IOS;
    }

    private String getDeviceName(HttpServletRequest request) {
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT).toLowerCase();
        String os = detectOS(userAgent);
        String device = detectDevice(userAgent);
        String browser = detectBrowser(userAgent);
        return String.format("%s%s%s", os,
                device.isEmpty() ? "" : " (" + device + ")",
                browser.isEmpty() ? "" : " · " + browser);
    }

    private String getIpAddress(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        return clientIp != null ? clientIp : request.getRemoteAddr();
    }

    private String generateDeviceId(HttpServletRequest request) {
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null) ipAddress = request.getRemoteAddr();
        String raw = userAgent + "|" + ipAddress;
        return UUID.nameUUIDFromBytes(raw.getBytes(StandardCharsets.UTF_8)).toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String detectOS(String userAgent) {
        return userAgent == null || userAgent.isBlank() ? "Unknown OS" :
                switch (userAgent.toLowerCase()) {
                    case String s when s.contains("windows nt 11") -> "Windows 11";
                    case String s when s.contains("windows nt 10") -> "Windows 10";
                    case String s when s.contains("windows nt 6.1") -> "Windows 7";
                    case String s when s.contains("mac os x") -> "Mac OS";
                    case String s when s.contains("android") -> "Android";
                    case String s when s.contains("iphone") -> "iOS";
                    case String s when s.contains("ipad") -> "iPadOS";
                    case String s when s.contains("linux") -> "Linux";
                    default -> "Unknown OS";
                };
    }

    private String detectDevice(String userAgent) {
        return userAgent == null || userAgent.isBlank() ? "Unknown Device" :
                switch (userAgent.toLowerCase()) {
                    case String s when s.contains("samsung") -> "Samsung Galaxy";
                    case String s when s.contains("sm-") -> "Samsung Device";
                    case String s when s.contains("pixel") -> "Google Pixel";
                    case String s when s.contains("iphone") -> "iPhone";
                    case String s when s.contains("ipad") -> "iPad";
                    case String s when s.contains("macintosh") -> "Mac";
                    case String s when s.contains("windows") -> "PC";
                    default -> "Unknown Device";
                };
    }

    private String detectBrowser(String userAgent) {
        return userAgent == null || userAgent.isBlank() ? "Unknown Browser" :
                switch (userAgent.toLowerCase()) {
                    case String s when s.contains("edg/") -> "Edge";
                    case String s when s.contains("chrome/") -> !s.contains("mobile") ? "Chrome" : "Chrome Mobile";
                    case String s when s.contains("safari/") && !s.contains("chrome") -> "Safari";
                    case String s when s.contains("firefox/") -> "Firefox";
                    default -> "Unknown Browser";
                };
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void printLog(JwtToken jwtToken, DeviceInfo deviceInfo) {
        log.info("Device Info: {}", deviceInfo.toString());
        log.info("Device ID: {}", deviceInfo.getDeviceId());
        log.info("Device Type: {}", deviceInfo.getDeviceType());
        log.info("Device Name: {}", deviceInfo.getDeviceName());
        log.info("Device IP Address: {}", deviceInfo.getIpAddress());
        log.info("Device User-Agent: {}", deviceInfo.getUserAgent());
        log.info("JWT Token: {}", jwtToken);
    }
}
