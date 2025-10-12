package com.loglab.livlog.postlike.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PostLikeDto {
    private Long postId;
    private Long userId;
}
