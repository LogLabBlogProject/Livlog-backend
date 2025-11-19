package com.loglab.livlog.media.controller;

import com.loglab.livlog.global.dto.CommonResponse;
import com.loglab.livlog.media.dto.MediaDto;
import com.loglab.livlog.media.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
@Tag(name = "Media API", description = "미디어 파일 업로드 및 관리 API")
public class MediaController {

    private final MediaService mediaService;

    @Operation(
            summary = "미디어 파일 업로드",
            description = "이미지 또는 파일을 업로드합니다. postId는 선택사항입니다."
    )
    @PostMapping("/upload")
    public ResponseEntity<CommonResponse<?>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "postId", required = false) Long postId) {
        MediaDto response = mediaService.uploadFile(file, postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "포스트의 미디어 목록 조회",
            description = "특정 포스트에 연결된 모든 미디어를 조회합니다"
    )
    @GetMapping("/post/{postId}")
    public ResponseEntity<CommonResponse<?>> getMediaByPost(@PathVariable Long postId) {
        List<MediaDto> response = mediaService.getByPostId(postId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "미디어 ID로 조회",
            description = "특정 미디어 정보를 조회합니다"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> getMedia(@PathVariable Long id) {
        MediaDto response = mediaService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "미디어 삭제",
            description = "미디어 파일을 삭제합니다"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> deleteMedia(@PathVariable Long id) {
        mediaService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("deleted"));
    }
}
