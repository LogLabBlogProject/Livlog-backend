package com.loglab.livlog.statistics.postlike.dto.request;

import lombok.*;

/**
 * 게시글(Post)에 대한 좋아요(Like) 추가/삭제 요청 DTO
 *
 * @author : 000flag
 * @fileName : PostLikeRequest
 * @since : 2025.11.14
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeRequest {
    /** 좋아요 대상 게시글 ID */
    private Long postId;

    /** 좋아요를 누르는 사용자 ID */
    private Long userId;
}