package com.loglab.livlog.statistics.post.dto.response;

import lombok.*;

/**
 * 일별 작성된 포스트 수를 나타내는 응답 DTO
 *
 * @author : 000flag
 * @fileName : DailyPostCountResponse
 * @since : 2025.11.11
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyPostCountResponse {
    /**
     * 작성 날짜 (yyyy-MM-dd)
     */
    private String date;

    /**
     * 해당 날짜의 작성된 포스트 수
     */
    private Long count;
}
