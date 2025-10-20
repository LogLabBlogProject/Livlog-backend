package com.loglab.livlog.bloginfo.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class BlogInfoDto {
    private Long id;
    private String title;
    private String description;
    private Long userId;
}
