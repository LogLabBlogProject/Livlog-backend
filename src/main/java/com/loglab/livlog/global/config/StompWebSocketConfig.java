package com.loglab.livlog.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompJwtChannelInterceptor stompJwtChannelInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")              // ws://.../ws
                .setAllowedOriginPatterns("*")   // CORS
                .withSockJS();                   // 필요 시
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독 prefix (브로커가 직접 전달)
        registry.enableSimpleBroker("/topic", "/queue");
        // 발행 prefix (컨트롤러 @MessageMapping 매핑)
        registry.setApplicationDestinationPrefixes("/app");
        // 1:1 귓속말시 /user/queue 사용도 가능
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompJwtChannelInterceptor);
    }
}
