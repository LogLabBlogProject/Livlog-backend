package com.loglab.livlog.statistics.postlike.controller;

import com.loglab.livlog.global.dto.CommonResponse;
import com.loglab.livlog.statistics.postlike.dto.request.PostLikeRequest;
import com.loglab.livlog.statistics.postlike.dto.response.PostLikeCountResponse;
import com.loglab.livlog.statistics.postlike.dto.response.PostLikeRankingResponse;
import com.loglab.livlog.statistics.postlike.service.PostLikeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 대시보드에서 사용하는 게시글(Post)에 대한 "좋아요(Like)" 기능 및 통계 Controller
 *
 * @author : 000flag
 * @fileName : CommentStatisticsController
 * @since : 2025.11.14
 */
@RestController
@RequestMapping("/api/posts/likes")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    /**
     * 좋아요 추가 API
     *
     * 요청 데이터:
     *   - postId : 좋아요를 누를 게시글 ID
     *   - userId : 좋아요를 누르는 사용자 ID
     *
     * 동작:
     *   - 이미 좋아요를 누른 상태라면 무시
     *   - 누르지 않은 상태라면 새로운 좋아요 레코드 생성
     *
     * @param request postId, userId 를 포함한 요청 DTO
     * @return CommonResponse<Void> 성공 여부
     */
    @Operation(
            summary = "좋아요 추가",
            description = "특정 게시글에 대해 사용자가 좋아요를 추가합니다."
    )
    @PostMapping("/add")
    public ResponseEntity<CommonResponse<Void>> addLike(@RequestBody PostLikeRequest request) {
        postLikeService.addLike(request);
        return ResponseEntity.ok(CommonResponse.success());
    }

    /**
     * 좋아요 취소 API
     *
     * 요청 데이터:
     *   - postId : 취소할 게시글 ID
     *   - userId : 좋아요를 취소하는 사용자 ID
     *
     * 동작:
     *   - 존재하는 좋아요 레코드가 있다면 삭제
     *   - 없다면 아무 작업도 하지 않음
     *
     * @param request postId, userId 를 포함한 요청 DTO
     * @return CommonResponse<Void>
     */
    @Operation(
            summary = "좋아요 취소",
            description = "특정 게시글에 대해 사용자가 누른 좋아요를 취소합니다."
    )
    @PostMapping("/remove")
    public ResponseEntity<CommonResponse<Void>> removeLike(@RequestBody PostLikeRequest request) {
        postLikeService.removeLike(request);
        return ResponseEntity.ok(CommonResponse.success());
    }

    /**
     * 특정 게시글의 좋아요 수 조회 API
     *
     * 사용처:
     *   - 게시글 상세 페이지
     *   - 관리자 대시보드(게시글 통계)
     *
     * @param postId 게시글 ID
     * @return CommonResponse<PostLikeCountResponse>
     */
    @Operation(
            summary = "게시글 좋아요 수 조회",
            description = "특정 게시글의 총 좋아요 개수를 조회합니다."
    )
    @GetMapping("/{postId}/count")
    public ResponseEntity<CommonResponse<PostLikeCountResponse>> getCount(
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(
                CommonResponse.success(postLikeService.getLikeCount(postId))
        );
    }

    /**
     * 좋아요 수 기준 인기 게시글 TOP N 조회 API
     *
     * 사용처:
     *   - 관리자 대시보드 인기글 분석
     *   - 추천글 노출
     *
     * @param top 조회할 게시글 수 (예: 5 → 인기글 TOP5)
     * @return CommonResponse<List<PostLikeRankingResponse>>
     */
    @Operation(
            summary = "인기 게시글 TOP N 조회",
            description = "좋아요 개수를 기준으로 가장 인기 있는 게시글을 N개 조회합니다."
    )
    @GetMapping("/top/{top}")
    public ResponseEntity<CommonResponse<List<PostLikeRankingResponse>>> getTop(
            @PathVariable int top
    ) {
        return ResponseEntity.ok(
                CommonResponse.success(postLikeService.getTopPosts(top))
        );
    }
}
