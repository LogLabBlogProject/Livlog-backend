package com.loglab.livlog.statistics.comment.controller;

import com.loglab.livlog.global.dto.CommonResponse;
import com.loglab.livlog.statistics.comment.dto.request.CommentStatsRequest;
import com.loglab.livlog.statistics.comment.dto.response.CommentBasicStatsResponse;
import com.loglab.livlog.statistics.comment.service.CommentStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자 대시보드에서 사용하는 댓글(Comment) 통계 Controller
 *
 * @author : 000flag
 * @fileName : CommentStatisticsController
 * @since : 2025.11.13
 */
@RestController
@RequestMapping("/api/admin/comments/stats")
@RequiredArgsConstructor
public class CommentStatisticsController {
    private final CommentStatisticsService commentStatisticsService;

    /**
     * 전체 댓글 수 조회 API.
     *
     * comment 테이블의 전체 레코드 개수를 조회하여 반환
     *
     * 사용처:
     *   - 관리자 대시보드 → 댓글 통계 카드 영역
     *
     * @return CommonResponse<CommentBasicStatsResponse>
     */
    @Operation(
            summary = "전체 댓글 수 조회",
            description = "현재까지 등록된 모든 댓글의 개수를 조회합니다."
    )
    @GetMapping("/basic")
    public ResponseEntity<CommonResponse<CommentBasicStatsResponse>> getBasic() {
        return ResponseEntity.ok(
                CommonResponse.success(commentStatisticsService.getBasicStats())
        );
    }

    /**
     * 작성자별 댓글 작성 수 TOP N 조회
     *
     * @param request top 값(N)을 포함한 요청 DTO
     * @return CommonResponse<List<CommentWriterTopResponse>>
     *
     * 사용처:
     *   - 관리자 대시보드 → 작성자 랭킹 테이블/그래프
     */
    @PostMapping("/top-writers")
    public ResponseEntity<CommonResponse<?>> getTopWriters(
            @RequestBody CommentStatsRequest request
    ) {
        return ResponseEntity.ok(
                CommonResponse.success(
                        commentStatisticsService.getTopWriters(request.getTop())
                )
        );
    }

    /**
     * 포스트별 댓글 수 TOP N 조회
     *
     * @param request top 값(N)을 포함한 요청 DTO
     * @return CommonResponse<List<PostCommentRankingResponse>>
     *
     * 사용처:
     *   - 관리자 대시보드 → 포스트별 댓글 순위 그래프
     */
    @PostMapping("/post-ranking")
    public ResponseEntity<CommonResponse<?>> getPostRanking(
            @RequestBody CommentStatsRequest request
    ) {
        return ResponseEntity.ok(
                CommonResponse.success(
                        commentStatisticsService.getPostRanking(request.getTop())
                )
        );
    }
}
