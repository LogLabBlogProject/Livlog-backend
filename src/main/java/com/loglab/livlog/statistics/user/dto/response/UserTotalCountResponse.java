package com.loglab.livlog.statistics.user.dto.response;

import lombok.*;

/**
 * 전체 사용자 누적 수를 반환하는 응답 DTO
 *
 * @author : 000flag
 * @fileName : UserTotalCountResponse
 * @since : 2025.11.05
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTotalCountResponse {
    /**
     * 누적 사용자 수
     */
    private Long totalUsers;
}
