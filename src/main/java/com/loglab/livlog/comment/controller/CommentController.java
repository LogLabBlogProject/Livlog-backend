package com.loglab.livlog.comment.controller;

import com.loglab.livlog.comment.dto.CommentDto;
import com.loglab.livlog.comment.entity.Comment;
import com.loglab.livlog.comment.service.CommentService;
import com.loglab.livlog.global.dto.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Comment API", description = "댓글 관련 CRUD API")
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "포스트ID로 댓글 조회",
            description = "특정 포스트의 모든 댓글을 조회합니다"
    )
    @GetMapping("/post/{postId}")
    public ResponseEntity<CommonResponse<?>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentDto> response = commentService.getCommentsByPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "댓글 ID로 조회",
            description = "특정 댓글을 조회합니다"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> getComment(@PathVariable Long id) {
        CommentDto response = commentService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "댓글 생성",
            description = "새로운 댓글을 생성합니다"
    )
    @PostMapping
    public ResponseEntity<CommonResponse<?>> createComment(@RequestBody Comment comment) {
        Comment created = commentService.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(created));
    }

    @Operation(
            summary = "댓글 수정",
            description = "댓글 내용을 수정합니다"
    )
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> updateComment(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String content = body.get("content");
        CommentDto response = commentService.update(id, content);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "댓글 삭제",
            description = "댓글을 삭제합니다"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> deleteComment(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("deleted"));
    }
}
