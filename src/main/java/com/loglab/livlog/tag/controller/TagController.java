package com.loglab.livlog.tag.controller;

import com.loglab.livlog.tag.dto.TagDto;
import com.loglab.livlog.tag.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
@Tag(name = "Tag API", description = "태그 관련 CRUD API")
public class TagController {
    private final TagService tagService;

    @Operation(
            summary = "모든 캐그 조회",
            description = ""
    )
    @GetMapping
    public List<TagDto> getTags() {
        return tagService.getAllTags();
    }

    @Operation(
            summary = "새 태그 등록",
            description = ""
    )
    @PostMapping
    public TagDto createTag(@RequestBody TagDto dto) {
        return tagService.createTag(dto);
    }
}
