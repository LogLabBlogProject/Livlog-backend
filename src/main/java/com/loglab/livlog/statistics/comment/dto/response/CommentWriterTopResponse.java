package com.loglab.livlog.statistics.comment.dto.response;

import lombok.*;

/**
 * 댓글 작성자별(Comment Writer) 댓글 수 통계를 담는 DTO
 *
 * @author : 000flag
 * @fileName : CommentWriterTopResponse
 * @since : 2025.11.13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentWriterTopResponse {
    /**
     * 댓글 작성자 ID
     */
    private Long userId;

    /**
     * 댓글 작성자 이름 또는 username
     */
    private String username;

    /**
     * 작성자가 작성한 댓글 수
     */
    private Long commentCount;
}