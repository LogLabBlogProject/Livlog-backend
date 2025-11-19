package com.loglab.livlog.statistics.post.dto.request;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 포스트 관련 기본 통계를 담는 응답 DTO
 *
 * @author : 000flag
 * @fileName : PostStatsRequest
 * @since : 2025.11.11
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostStatsRequest {
    /**
     * 조회 기간 시작 날짜/시간
     */
    private LocalDateTime startDate;

    /**
     * 조회 기간 종료 날짜/시간
     */
    private LocalDateTime endDate;
}
