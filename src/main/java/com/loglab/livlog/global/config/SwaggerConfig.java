package com.loglab.livlog.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Livlog API Docs")
                        .description("블로그 플랫폼 API 명세서 (Tag, Comment, Media, BlogInfo, PostLike 등)")
                        .version("1.0.0"));
    }
}
