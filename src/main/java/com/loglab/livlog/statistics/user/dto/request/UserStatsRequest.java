package com.loglab.livlog.statistics.user.dto.request;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 통계 요청 시 기간(startDate ~ endDate)을 담아서 전달하는 Request DTO
 *
 * @author : 000flag
 * @fileName : UserStatsRequest
 * @since : 2025.11.05
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsRequest {
    /**
     * 조회 시작 날짜·시간
     */
    private LocalDateTime startDate;

    /**
     * 조회 종료 날짜·시간
     */
    private LocalDateTime endDate;
}
