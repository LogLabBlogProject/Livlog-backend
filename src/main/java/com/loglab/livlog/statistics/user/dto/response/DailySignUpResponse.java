package com.loglab.livlog.statistics.user.dto.response;

import lombok.*;

/**
 * 일별 신규 가입자 수를 표현하는 응답 DTO
 *
 * @author : 000flag
 * @fileName : DailySignUpResponse
 * @since : 2025.11.05
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailySignUpResponse {
    /**
     * 가입 날짜 (yyyy-MM-dd)
     */
    private String date;

    /**
     * 해당 날짜의 가입자 수
     */
    private Long count;
}
