package com.loglab.livlog.comment.service;

import com.loglab.livlog.comment.dto.CommentDto;
import com.loglab.livlog.comment.entity.Comment;
import com.loglab.livlog.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
}
