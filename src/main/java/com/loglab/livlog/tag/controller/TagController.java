package com.loglab.livlog.tag.controller;

import com.loglab.livlog.global.dto.CommonResponse;
import com.loglab.livlog.tag.dto.TagDto;
import com.loglab.livlog.tag.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
@Tag(name = "Tag API", description = "태그 관련 CRUD API")
public class TagController {
    private final TagService tagService;

    @Operation(
            summary = "모든 태그 조회",
            description = "모든 태그 목록을 조회합니다"
    )
    @GetMapping
    public ResponseEntity<CommonResponse<?>> getTags() {
        List<TagDto> response = tagService.getAllTags();
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "태그 ID로 조회",
            description = "특정 태그를 조회합니다"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> getTag(@PathVariable Long id) {
        TagDto response = tagService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "새 태그 등록",
            description = "새로운 태그를 생성합니다"
    )
    @PostMapping
    public ResponseEntity<CommonResponse<?>> createTag(@RequestBody TagDto dto) {
        TagDto response = tagService.createTag(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "태그 수정",
            description = "태그 이름을 수정합니다"
    )
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> updateTag(@PathVariable Long id, @RequestBody TagDto dto) {
        TagDto response = tagService.updateTag(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "태그 삭제",
            description = "태그를 삭제합니다"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("deleted"));
    }
}
