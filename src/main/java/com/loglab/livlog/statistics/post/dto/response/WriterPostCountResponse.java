package com.loglab.livlog.statistics.post.dto.response;

import lombok.*;

/**
 * 작성자별 포스트 작성 수를 담는 응답 DTO
 *
 * @author : 000flag
 * @fileName : WriterPostCountResponse
 * @since : 2025.11.11
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WriterPostCountResponse {
    /**
     * 작성자 ID
     */
    private Long userId;

    /**
     * 작성자 이름(또는 username)
     */
    private String username;

    /**
     * 작성자 작성 포스트 수
     */
    private Long postCount;
}
