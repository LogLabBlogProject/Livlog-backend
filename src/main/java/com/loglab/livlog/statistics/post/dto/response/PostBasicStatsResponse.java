package com.loglab.livlog.statistics.post.dto.response;

import lombok.*;

/**
 * 포스트 관련 기본 통계를 담는 응답 DTO
 *
 * @author : 000flag
 * @fileName : PostBasicStatsResponse
 * @since : 2025.11.11
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostBasicStatsResponse {
    /**
     * 전체 포스트 수
     */
    private long totalPosts;

    /**
     * 조회 기간(startDate ~ endDate) 동안 작성된 포스트 수
     */
    private long postsInRange;

    /**
     * deleted_at 이 null 이 아닌 포스트의 개수(soft delete)
     */
    private long deletedPosts;

    /**
     * 평균 글 길이 (단어 수 기준)
     */
    private double averageWordCount;
}
