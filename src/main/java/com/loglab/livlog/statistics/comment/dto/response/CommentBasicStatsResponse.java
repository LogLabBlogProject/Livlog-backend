package com.loglab.livlog.statistics.comment.dto.response;

import lombok.*;

/**
 * 댓글(Comment) 기본 통계 응답 DTO
 *
 * @author : 000flag
 * @fileName : CommentBasicStatsResponse
 * @since : 2025.11.13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentBasicStatsResponse {
    /**
     * 전체 댓글 수
     */
    private long totalComments;
}
