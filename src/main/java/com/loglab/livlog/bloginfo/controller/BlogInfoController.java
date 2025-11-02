package com.loglab.livlog.bloginfo.controller;

import com.loglab.livlog.bloginfo.dto.BlogInfoDto;
import com.loglab.livlog.bloginfo.service.BlogInfoService;
import com.loglab.livlog.global.dto.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bloginfo")
@RequiredArgsConstructor
@Tag(name = "BlogInfo API", description = "블로그 정보 관련 CRUD API")
public class BlogInfoController {

    private final BlogInfoService blogInfoService;

    @Operation(
            summary = "유저ID로 블로그 정보 조회",
            description = "특정 사용자의 블로그 정보를 조회합니다"
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<CommonResponse<?>> getBlogInfo(@PathVariable Long userId) {
        BlogInfoDto response = blogInfoService.getByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "블로그 정보 생성",
            description = "새로운 블로그 정보를 생성합니다"
    )
    @PostMapping
    public ResponseEntity<CommonResponse<?>> create(@RequestBody BlogInfoDto dto) {
        BlogInfoDto response = blogInfoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "블로그 정보 수정",
            description = "기존 블로그 정보를 수정합니다"
    )
    @PutMapping("/user/{userId}")
    public ResponseEntity<CommonResponse<?>> update(@PathVariable Long userId, @RequestBody BlogInfoDto dto) {
        BlogInfoDto response = blogInfoService.update(userId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "블로그 정보 삭제",
            description = "블로그 정보를 삭제합니다"
    )
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<CommonResponse<?>> delete(@PathVariable Long userId) {
        blogInfoService.delete(userId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("deleted"));
    }
}
