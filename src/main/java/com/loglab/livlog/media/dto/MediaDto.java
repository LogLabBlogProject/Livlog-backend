package com.loglab.livlog.media.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class MediaDto {
    private Long id;
    private String fileName;
    private String fileType;
    private String url;
    private Long postId;
}
