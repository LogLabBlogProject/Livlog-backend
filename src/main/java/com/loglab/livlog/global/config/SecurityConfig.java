package com.loglab.livlog.global.config;

import com.loglab.livlog.auth.jwt.JwtAccessDeniedHandler;
import com.loglab.livlog.auth.jwt.JwtAuthenticationEntryPoint;
import com.loglab.livlog.auth.oauth.handler.CustomOAuth2AuthenticationFailureHandler;
import com.loglab.livlog.auth.oauth.handler.CustomOAuth2AuthenticationSuccessHandler;
import com.loglab.livlog.auth.oauth.service.CustomOAuth2OidcUserService;
import com.loglab.livlog.auth.jwt.JwtTokenFilter;
import com.loglab.livlog.auth.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2OidcUserService customOAuth2OidcUserService;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;
    private final CustomOAuth2AuthenticationFailureHandler customOAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            ObjectProvider<ClientRegistrationRepository> clientRepoProvider
    ) throws Exception {

        ClientRegistrationRepository clientRepo = clientRepoProvider.getIfAvailable();

        http
                .csrf(AbstractHttpConfigurer::disable)
                // OAuth2를 안 쓸 때는 완전 무상태로 운용(JWT). OAuth2를 켜더라도 성공 시 JWT 발급 후 세션 미사용이면 유지 가능.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/login", "/error",
                                "/oauth2/**", "/login/oauth2/**"       // OAuth2 엔드포인트 허용
                        ).permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 등록 정보가 있을 때만 소셜 로그인 활성화
        if (clientRepo != null && hasAnyClient(clientRepo)) {
            http.oauth2Login(oauth2 -> oauth2
                    .userInfoEndpoint(userInfo -> userInfo
                            .userService(customOAuth2UserService)
                            .oidcUserService(customOAuth2OidcUserService)
                    )
                    .successHandler(customOAuth2AuthenticationSuccessHandler)
                    .failureHandler(customOAuth2AuthenticationFailureHandler)
            );
        }

        return http.build();
    }

    // TODO : OAuth 추가 전까지 코드 주석처리
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/login", "/error").permitAll()
//                        .requestMatchers("/api/**").authenticated()
//                        .anyRequest().authenticated()
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
//                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOAuth2OidcUserService))
//                        .successHandler(customOAuth2AuthenticationSuccessHandler)
//                        .failureHandler(customOAuth2AuthenticationFailureHandler)
////                        .defaultSuccessUrl("/home", true)
////                        .failureUrl("/login?error=true")
//                )
//                .exceptionHandling(exception -> exception
//                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                        .accessDeniedHandler(jwtAccessDeniedHandler)
//                )
//                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }


    // InMemoryClientRegistrationRepository면 실제 등록이 있는지 체크
    private boolean hasAnyClient(ClientRegistrationRepository repo) {
        if (repo instanceof InMemoryClientRegistrationRepository mem) {
            return mem.iterator().hasNext();
        }
        // 다른 구현체면 존재 자체로 충분하다고 판단
        return true;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
