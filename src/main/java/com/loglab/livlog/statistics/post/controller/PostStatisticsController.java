package com.loglab.livlog.statistics.post.controller;

import com.loglab.livlog.global.dto.CommonResponse;
import com.loglab.livlog.statistics.post.dto.response.DailyPostCountResponse;
import com.loglab.livlog.statistics.post.dto.response.PostBasicStatsResponse;
import com.loglab.livlog.statistics.post.dto.request.PostStatsRequest;
import com.loglab.livlog.statistics.post.dto.response.WriterPostCountResponse;
import com.loglab.livlog.statistics.post.service.PostStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 대시보드에서 사용하는 포스트 통계 Controller
 *
 * @author : 000flag
 * @fileName : PostStatisticsController
 * @since : 2025.11.11
 */
@RestController
@RequestMapping("/api/admin/posts/stats")
@RequiredArgsConstructor
public class PostStatisticsController {

    private final PostStatisticsService postService;

    /**
     * 기본 통계 조회
     *
     * 조회 항목:
     *   - 전체 포스트 수
     *   - 기간 내 작성 포스트 수
     *   - 삭제된 포스트 수 (soft delete)
     *   - 평균 글 길이(단어 수)
     *
     * @param request startDate, endDate 를 포함
     * @return 기본 통계(PostBasicStatsResponse)
     */
    @Operation(
            summary = "포스트 기본 통계 조회",
            description = "전체 글 수, 삭제된 글 수, 기간 내 작성 포스트 수, 평균 글 길이(단어 수)를 조회합니다."
    )
    @PostMapping("/basic")
    public ResponseEntity<CommonResponse<PostBasicStatsResponse>> getBasicStats(
            @RequestBody PostStatsRequest request) {

        var data = postService.getBasicStats(request.getStartDate(), request.getEndDate());
        return ResponseEntity.ok(CommonResponse.success(data));
    }

    /**
     * 일별 포스트 작성 수 통계 조회
     *
     * 사용처:
     *   - 관리자 대시보드 → Line/Bar 그래프
     *
     * @param request startDate, endDate 를 포함
     * @return 날짜별 글 수 리스트
     */
    @Operation(
            summary = "일별 포스트 작성 건수 조회",
            description = "기간 내 작성된 포스트를 날짜별로 집계하여 그래프용 데이터를 반환합니다."
    )
    @PostMapping("/daily")
    public ResponseEntity<CommonResponse<List<DailyPostCountResponse>>> getDailyStats(
            @RequestBody PostStatsRequest request) {

        var data = postService.getDailyPosts(request.getStartDate(), request.getEndDate());
        return ResponseEntity.ok(CommonResponse.success(data));
    }

    /**
     * 작성자별 포스트 수 TOP 10 조회
     *
     * 사용처:
     *   - 관리자 대시보드 → 작성자 순위 그래프
     *
     * @return Top10 작성자 및 작성 수
     */
    @Operation(
            summary = "작성자별 포스트 수 TOP 10",
            description = "전체 포스트를 기준으로 작성자별 게시글 수를 집계하여 상위 10명을 반환합니다."
    )
    @GetMapping("/top-writers")
    public ResponseEntity<CommonResponse<List<WriterPostCountResponse>>> getTopWriters() {
        return ResponseEntity.ok(CommonResponse.success(postService.getTopWriters()));
    }
}
