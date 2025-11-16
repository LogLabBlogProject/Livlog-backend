package com.loglab.livlog.statistics.postlike.dto.response;

import lombok.*;

/**
 * 특정 게시글의 좋아요(Like) 개수를 반환하는 응답 DTO
 *
 * @author : 000flag
 * @fileName : PostLikeCountResponse
 * @since : 2025.11.14
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLikeCountResponse {
    /** 게시글 ID */
    private Long postId;

    /** 게시글의 총 좋아요 수 */
    private Long likeCount;
}
