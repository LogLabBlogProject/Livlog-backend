package com.loglab.livlog.global.config;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.redis.host}") private String host;
    @Value("${spring.redis.port}") private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory cf) {
        return new StringRedisTemplate(cf);
    }

    @Bean
    public ChannelTopic chatTopic() {
        return new ChannelTopic("chat"); // 단일 채널. payload 안에 roomId 포함
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory cf,
                                                   MessageListenerAdapter adapter,
                                                   ChannelTopic chatTopic) {
        RedisMessageListenerContainer c = new RedisMessageListenerContainer();
        c.setConnectionFactory(cf);
        c.addMessageListener(adapter, chatTopic);
        return c;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(RedisChatSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }
}
