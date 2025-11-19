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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler implements com.loglab.livlog.auth.oauth.handler.AuthenticationSuccessHandler {
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
                .map((token) -> {
                    JwtToken update = token.update(JwtTokenUpdateRequestDto.builder()
                            .deviceInfo(deviceInfo)
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .accessTokenIssuedAt(accessTokenClaims.getIssuedAt().toInstant())
                            .accessTokenExpiresAt(accessTokenClaims.getExpiration().toInstant())
                            .refreshTokenIssuedAt(refreshTokenClaims.getIssuedAt().toInstant())
                            .refreshTokenExpiresAt(refreshTokenClaims.getExpiration().toInstant())
                            .build());
                    return update;
                })
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

        /* JWT 토큰 및 디바이스 정보 저장 */
        printLog(jwtToken, deviceInfo);
        jwtTokenRepository.save(jwtToken);
        CommonResponse<?> successResponse = CommonResponse.success(
                JwtTokenResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build()
        );

        String json = new ObjectMapper().writeValueAsString(successResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(json);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
        if (!userAgent.contains("mobile")) return DeviceInfo.DeviceType.WEB;
        if (userAgent.contains("android")) return DeviceInfo.DeviceType.ANDROID;
        return DeviceInfo.DeviceType.IOS;
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String detectOS(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) return "Unknown OS";
        String ua = userAgent.toLowerCase();
        if (ua.contains("windows nt 11")) return "Windows 11";
        else if (ua.contains("windows nt 10")) return "Windows 10";
        else if (ua.contains("windows nt 6.1")) return "Windows 7";
        else if (ua.contains("mac os x")) return "Mac OS";
        else if (ua.contains("android")) return "Android";
        else if (ua.contains("iphone")) return "iOS";
        else if (ua.contains("ipad")) return "iPadOS";
        else if (ua.contains("linux")) return "Linux";
        else return "Unknown OS";
    }

    private String detectDevice(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) return "Unknown Device";
        String ua = userAgent.toLowerCase();
        if (ua.contains("samsung")) return "Samsung Galaxy";
        else if (ua.contains("sm-")) return "Samsung Device";
        else if (ua.contains("pixel")) return "Google Pixel";
        else if (ua.contains("iphone")) return "iPhone";
        else if (ua.contains("ipad")) return "iPad";
        else if (ua.contains("macintosh")) return "Mac";
        else if (ua.contains("windows")) return "PC";
        else return "Unknown Device";
    }

    private String detectBrowser(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) return "Unknown Browser";
        String ua = userAgent.toLowerCase();
        if (ua.contains("edg/")) return "Edge";
        else if (ua.contains("chrome/") && !ua.contains("mobile")) return "Chrome";
        else if (ua.contains("chrome/") && ua.contains("mobile")) return "Chrome Mobile";
        else if (ua.contains("safari/") && !ua.contains("chrome")) return "Safari";
        else if (ua.contains("firefox/")) return "Firefox";
        else return "Unknown Browser";
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
