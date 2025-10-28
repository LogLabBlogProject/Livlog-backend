package com.loglab.livlog.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "댓글 DTO")
public class CommentDto {
    private Long id;
    private Long postId;
    private Long userId;
    private Long parentId;
    private String content;
}
