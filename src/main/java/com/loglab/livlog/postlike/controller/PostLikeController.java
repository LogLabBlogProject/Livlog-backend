package com.loglab.livlog.postlike.controller;

import com.loglab.livlog.global.dto.CommonResponse;
import com.loglab.livlog.postlike.dto.PostLikeDto;
import com.loglab.livlog.postlike.service.PostLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
@Tag(name = "PostLike API", description = "게시글 좋아요 관련 API")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @Operation(
            summary = "좋아요 토글",
            description = "게시글에 좋아요를 추가하거나 취소합니다"
    )
    @PostMapping("/toggle")
    public ResponseEntity<CommonResponse<?>> toggleLike(@RequestBody PostLikeDto dto) {
        PostLikeDto response = postLikeService.toggleLike(dto.getPostId(), dto.getUserId());
        String message = response == null ? "좋아요 취소" : "좋아요 추가";
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(Map.of(
                "message", message,
                "data", response
        )));
    }

    @Operation(
            summary = "게시글의 좋아요 수 조회",
            description = "특정 게시글의 총 좋아요 개수를 조회합니다"
    )
    @GetMapping("/count/{postId}")
    public ResponseEntity<CommonResponse<?>> getLikeCount(@PathVariable Long postId) {
        Long count = postLikeService.getLikeCount(postId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(Map.of("count", count)));
    }

    @Operation(
            summary = "게시글의 좋아요 목록 조회",
            description = "특정 게시글에 좋아요를 누른 사용자 목록을 조회합니다"
    )
    @GetMapping("/post/{postId}")
    public ResponseEntity<CommonResponse<?>> getLikesByPost(@PathVariable Long postId) {
        List<PostLikeDto> response = postLikeService.getLikesByPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "사용자가 좋아요한 게시글 목록",
            description = "특정 사용자가 좋아요를 누른 모든 게시글을 조회합니다"
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<CommonResponse<?>> getLikesByUser(@PathVariable Long userId) {
        List<PostLikeDto> response = postLikeService.getLikesByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "게시글 좋아요 정보 조회",
            description = "게시글의 좋아요 수와 특정 사용자의 좋아요 여부를 조회합니다"
    )
    @GetMapping("/info")
    public ResponseEntity<CommonResponse<?>> getPostLikeInfo(
            @RequestParam Long postId,
            @RequestParam Long userId) {
        Map<String, Object> info = postLikeService.getPostLikeInfo(postId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(info));
    }
}
