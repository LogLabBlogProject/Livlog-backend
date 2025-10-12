package com.loglab.livlog.comment.controller;

import com.loglab.livlog.comment.dto.CommentDto;
import com.loglab.livlog.comment.entity.Comment;
import com.loglab.livlog.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Comment API", description = "댓글 관련 CRUD API")
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "포스트ID로 댓글 조회",
            description = ""
    )
    @GetMapping("/{postId}")
    public List<CommentDto> getComments(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }

    @Operation(
            summary = "댓글 생성",
            description = ""
    )
    @PostMapping
    public Comment createComment(@RequestBody Comment comment) {
        return commentService.save(comment);
    }
}
