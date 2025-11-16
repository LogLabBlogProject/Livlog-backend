package com.loglab.livlog.statistics.comment.dto.response;

import lombok.*;

/**
 * 포스트(Post)별 댓글 수 TOP N 통계를 담는 DTO
 *
 * @author : 000flag
 * @fileName : PostCommentRankingResponse
 * @since : 2025.11.13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentRankingResponse {
    /**
     * 포스트 ID
     */
    private Long postId;

    /**
     * 포스트 제목
     */
    private String postTitle;

    /**
     * 해당 포스트에 달린 댓글 수
     */
    private Long commentCount;
}