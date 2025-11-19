package com.loglab.livlog.post.controller;

import com.loglab.livlog.global.dto.CommonResponse;
import com.loglab.livlog.post.dto.PostCreateRequestDto;
import com.loglab.livlog.post.dto.PostDetailResponseDto;
import com.loglab.livlog.post.dto.PostSimpleResponseDto;
import com.loglab.livlog.post.dto.PostUpdateRequestDto;
import com.loglab.livlog.post.service.PostReadService;
import com.loglab.livlog.post.service.PostWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Post API", description = "포스트 관련 CRUD API")
public class PostController {

    private final PostReadService postReadService;
    private final PostWriteService postWriteService;

    @Operation(
            summary = "포스트ID로 상세 조회",
            description = ""
    )
    @GetMapping("/api/v1/post/detail/{id}")
    public ResponseEntity<CommonResponse<?>> getPost(@PathVariable Long id) {
        PostDetailResponseDto responseDto = postReadService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(responseDto));
    }

    @Operation(
            summary = "포스트 전체 조회",
            description = ""
    )
    @GetMapping("/api/v1/post/list")
    public ResponseEntity<CommonResponse<?>> getPosts() {
        List<PostSimpleResponseDto> responseDto = postReadService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(responseDto));
    }

    @Operation(
            summary = "포스트 검색",
            description = ""
    )
    @GetMapping("/api/v1/post/search")
    public ResponseEntity<CommonResponse<?>> searchPosts(@RequestBody Map<String, Object> params) {
        List<PostSimpleResponseDto> responseDto = postReadService.searchBy(params);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(responseDto));
    }

    @Operation(
            summary = "포스트 생성",
            description = ""
    )
    @PostMapping("/api/v1/post/create")
    public ResponseEntity<CommonResponse<?>> createPost(@RequestBody PostCreateRequestDto requestDto) {
        PostDetailResponseDto responseDto = postWriteService.create(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(responseDto));
    }

    @Operation(
            summary = "포스트 수정",
            description = ""
    )
    @PutMapping("/api/v1/post/update")
    public ResponseEntity<CommonResponse<?>> updatePost(@RequestBody PostUpdateRequestDto requestDto) {
        PostDetailResponseDto responseDto = postWriteService.update(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(responseDto));
    }

    @Operation(
            summary = "포스트 삭제",
            description = ""
    )
    @DeleteMapping("/api/v1/post/delete/{id}")
    public ResponseEntity<CommonResponse<?>> deletePost(@PathVariable Long id) {
        PostDetailResponseDto responseDto = postWriteService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(responseDto));
    }

    @Operation(
            summary = "유저ID로 유저 포스트 조회",
            description = "특정 사용자의 모든 포스트를 조회합니다"
    )
    @GetMapping("/api/v1/post/user/{userId}")
    public ResponseEntity<CommonResponse<?>> getPostsByUser(@PathVariable Long userId) {
        List<PostSimpleResponseDto> responseDto = postReadService.findByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(responseDto));
    }

    @Operation(
            summary = "카테고리ID로 포스트 조회",
            description = "특정 카테고리의 모든 포스트를 조회합니다"
    )
    @GetMapping("/api/v1/post/category/{categoryId}")
    public ResponseEntity<CommonResponse<?>> getPostsByCategory(@PathVariable Long categoryId) {
        List<PostSimpleResponseDto> responseDto = postReadService.findByCategoryId(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(responseDto));
    }

    @Operation(
            summary = "태그ID로 포스트 조회",
            description = "특정 태그가 포함된 모든 포스트를 조회합니다"
    )
    @GetMapping("/api/v1/post/tag/{tagId}")
    public ResponseEntity<CommonResponse<?>> getPostsByTag(@PathVariable Long tagId) {
        List<PostSimpleResponseDto> responseDto = postReadService.findByTagId(tagId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(responseDto));
    }

    @Operation(
            summary = "최신 포스트 조회",
            description = "최신 포스트를 생성일 기준 내림차순으로 조회합니다"
    )
    @GetMapping("/api/v1/post/latest")
    public ResponseEntity<CommonResponse<?>> getLatestPosts() {
        List<PostSimpleResponseDto> responseDto = postReadService.findLatestPosts();
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(responseDto));
    }

}
