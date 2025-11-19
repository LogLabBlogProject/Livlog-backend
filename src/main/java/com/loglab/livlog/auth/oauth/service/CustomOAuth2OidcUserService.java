package com.loglab.livlog.auth.oauth.service;

import com.loglab.livlog.auth.jwt.CustomUserDetails;
import com.loglab.livlog.auth.oauth.dto.*;
import com.loglab.livlog.auth.oauth.entity.OAuthProvider;
import com.loglab.livlog.user.entity.Role;
import com.loglab.livlog.user.entity.User;
import com.loglab.livlog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2OidcUserService extends OidcUserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuthProvider provider = OAuthProvider.valueOf(registrationId.toUpperCase());
        return processOAuth2User(provider, oidcUser);
    }

    private OidcUser processOAuth2User(OAuthProvider provider, OidcUser oidcUser) {
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(provider, oidcUser.getAttributes());
        log.info("OAUTH2USERINFO ABSTRACT CLASS FROM CUSTOM OIDC USER SERVICE: {}", oidcUser);

        // 사용자 처리 로직
        // TODO: 추후 map() UserUpdateRequestDto로 변경 필요, SuccessHandler 참고
        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail())
                .map(u -> u.update(oAuth2UserInfo))
                .orElseGet(() -> User.builder()
                        .email(oAuth2UserInfo.getEmail())
                        .username(oAuth2UserInfo.getName())
                        .nickname(oAuth2UserInfo.getName())
                        .profile(oAuth2UserInfo.getImageUrl())
                        .provider(provider)
                        .providerId(oAuth2UserInfo.getId())
                        .role(Role.USER)
                        .emailVerified(true)
                        .build());
        userRepository.save(user);
        return new CustomUserDetails(user, oidcUser.getAttributes());
    }

    private OAuth2UserInfo getOAuth2UserInfo(OAuthProvider provider, Map<String, Object> attributes) {
        return switch (provider) {
            case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
            case NAVER -> new NaverOAuth2UserInfo(attributes);
            case KAKAO -> new KakaoOAuth2UserInfo(attributes);
            case GITHUB -> new GithubOAuth2UserInfo(attributes);
            case TWITTER -> new TwitterOAuth2UserInfo(attributes);
            case FACEBOOK -> new FacebookOAuth2UserInfo(attributes);
            case LINKEDIN -> new LinkedInOAuth2UserInfo(attributes);
            default -> throw new OAuth2AuthenticationException("Unsupported provider: " + provider.name());
        };
    }

}
