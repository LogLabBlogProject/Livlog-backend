package com.loglab.livlog.statistics.postlike.dto.response;

import lombok.*;

/**
 * 좋아요(Like) 개수 기준 TOP N 인기 게시글 랭킹 정보를 반환하는 DTO
 *
 * @author : 000flag
 * @fileName : PostLikeRankingResponse
 * @since : 2025.11.14
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLikeRankingResponse {
    /** 게시글 ID */
    private Long postId;

    /** 게시글 제목 */
    private String postTitle;

    /** 해당 게시글의 좋아요 수 */
    private Long likeCount;
}