package com.loglab.livlog.tag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "태그 정보 DTO")
public class TagDto {
    @Schema(description = "태그 ID", example = "1")
    private Long id;

    @Schema(description = "태그 이름", example = "Spring Boot")
    private String name;
}
