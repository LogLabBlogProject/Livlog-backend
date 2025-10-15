package com.loglab.livlog.post.service;


import com.loglab.livlog.global.exception.CustomNotFoundException;
import com.loglab.livlog.global.exception.CustomValidateException;
import com.loglab.livlog.post.dto.PostCreateRequestDto;
import com.loglab.livlog.post.dto.PostDetailResponseDto;
import com.loglab.livlog.post.dto.PostUpdateRequestDto;
import com.loglab.livlog.post.entity.Post;
import com.loglab.livlog.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostWriteService {

    private final PostRepository postRepository;

    public PostDetailResponseDto create(PostCreateRequestDto requestDto) {
        if (requestDto.getTitle() == null || requestDto.getTitle().isEmpty())
            throw new CustomValidateException("The post title must not be empty.");
        if (requestDto.getContent() == null || requestDto.getContent().isEmpty())
            throw new CustomValidateException("The post content must not be empty.");
        return new PostDetailResponseDto(postRepository.save(requestDto.toEntity()));
    }

    public PostDetailResponseDto update(PostUpdateRequestDto requestDto) {
        if (requestDto.getTitle() == null || requestDto.getTitle().isEmpty())
            throw new CustomValidateException("The post title must not be empty.");
        if (requestDto.getContent() == null || requestDto.getContent().isEmpty())
            throw new CustomValidateException("The post content must not be empty.");

        Post entity = postRepository.findById(requestDto.getId()).orElseThrow(
                () -> new CustomNotFoundException("Post not found with id: " + requestDto.getId()));
        return new PostDetailResponseDto(postRepository.save(entity.update(requestDto)));
    }

    public PostDetailResponseDto delete(Long id) {
        Post entity = postRepository.findById(id).orElseThrow(
                () -> new CustomNotFoundException("Post not found with id: " + id));
        postRepository.deleteById(id);
        return new PostDetailResponseDto(entity);
    }
}
