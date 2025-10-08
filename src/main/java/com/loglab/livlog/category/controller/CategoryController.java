package com.loglab.livlog.category.controller;

import com.loglab.livlog.category.entity.Category;
import com.loglab.livlog.category.service.CategoryService;
import com.loglab.livlog.global.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
@Tag(name = "Category API", description = "카테고리 관련 CRUD API")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "유저ID로 카테고리 조회",
            description = ""
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<CommonResponse<?>> getCategoriesByUser(@PathVariable Long userId) {
        List<Category> response = categoryService.findAllByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "카테고리 생성",
            description = ""
    )
    @PostMapping("/create")
    public ResponseEntity<CommonResponse<?>> create(@RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success(categoryService.create(category)));
    }

    @Operation(
            summary = "카테고리ID로 카테고리 수정",
            description = ""
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<CommonResponse<?>> update(@PathVariable Long id, @RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success(categoryService.update(id, category.getName())));
    }

    @Operation(
            summary = "카테고리ID로 카테고리 삭제",
            description = ""
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<?>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("deleted"));
    }
}
