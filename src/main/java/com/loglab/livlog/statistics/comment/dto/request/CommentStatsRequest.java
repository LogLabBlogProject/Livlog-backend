package com.loglab.livlog.statistics.comment.dto.request;

import lombok.*;

/**
 * 댓글(Comment) 통계 조회 시 전달받는 요청 DTO
 *
 * @author : 000flag
 * @fileName : CommentStatsRequest
 * @since : 2025.11.13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentStatsRequest {
    /**
     * 조회할 상위 N개 (TOP N)
     */
    private Integer top;
}
