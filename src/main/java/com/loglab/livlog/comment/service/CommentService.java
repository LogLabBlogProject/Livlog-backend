package com.loglab.livlog.comment.service;

import com.loglab.livlog.comment.dto.CommentDto;
import com.loglab.livlog.comment.entity.Comment;
import com.loglab.livlog.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    public List<CommentDto> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(c -> new CommentDto(c.getId(), postId,
                        c.getUser().getId(),
                        c.getParentComment() != null ? c.getParentComment().getId() : null,
                        c.getContent()))
                .collect(Collectors.toList());
    }

    public CommentDto getById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found: " + id));
        return new CommentDto(
                comment.getId(),
                comment.getPost().getId(),
                comment.getUser().getId(),
                comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                comment.getContent()
        );
    }

    @Transactional
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    public CommentDto update(Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found: " + id));
        comment.setContent(content);
        Comment updated = commentRepository.save(comment);
        return new CommentDto(
                updated.getId(),
                updated.getPost().getId(),
                updated.getUser().getId(),
                updated.getParentComment() != null ? updated.getParentComment().getId() : null,
                updated.getContent()
        );
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found: " + id));
        commentRepository.delete(comment);
    }
}
