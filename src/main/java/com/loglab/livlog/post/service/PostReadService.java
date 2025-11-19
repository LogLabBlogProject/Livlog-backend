package com.loglab.livlog.post.service;


import com.loglab.livlog.global.exception.CustomNotFoundException;
import com.loglab.livlog.post.dto.PostDetailResponseDto;
import com.loglab.livlog.post.dto.PostSimpleResponseDto;
import com.loglab.livlog.post.entity.Post;
import com.loglab.livlog.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostReadService {

    private final PostRepository postRepository;

    /** 상세 조회 메서드 */
    public PostDetailResponseDto findById(Long id) {
        Post entity = postRepository.findById(id).orElseThrow(
                () -> new CustomNotFoundException("Post not found with id: " + id));
        return new PostDetailResponseDto(entity);
    }

    /** 목록 조회 메서드 */
    public List<PostSimpleResponseDto> findAll() {
        return postRepository.findAll().stream().map(PostSimpleResponseDto::new).toList();
    }

    /** 검색 조회 메서드 */
    public List<PostSimpleResponseDto> searchBy(Map<String, Object> params) {
        String title = (String) params.get("title");
        String content = (String) params.get("content");
        LocalDateTime sCreatedDate = params.containsKey("sCreatedDate") ?
                LocalDateTime.parse((String) params.get("sCreatedDate")) : null;
        LocalDateTime eCreatedDate = params.containsKey("eCreatedDate") ?
                LocalDateTime.parse((String) params.get("eCreatedDate")) : null;
        LocalDateTime sUpdatedDate = params.containsKey("sUpdatedDate") ?
                LocalDateTime.parse((String) params.get("sUpdatedDate")) : null;
        LocalDateTime eUpdatedDate = params.containsKey("eUpdatedDate") ?
                LocalDateTime.parse((String) params.get("eUpdatedDate")) : null;
        return postRepository.searchBy(title, content, sCreatedDate, eCreatedDate, sUpdatedDate, eUpdatedDate)
                .stream().map(PostSimpleResponseDto::new).toList();
    }

    /** 특정 사용자(userId) 기준 게시글 목록 조회 */
    public List<PostSimpleResponseDto> findByUserId(Long userId) {
        return postRepository.findByUserId(userId)
                .stream()
                .map(PostSimpleResponseDto::new)
                .toList();
    }

    /** 카테고리별 게시글 목록 조회 */
    public List<PostSimpleResponseDto> findByCategoryId(Long categoryId) {
        return postRepository.findByCategoryId(categoryId)
                .stream()
                .map(PostSimpleResponseDto::new)
                .toList();
    }

    /** 태그별 게시글 목록 조회 */
    public List<PostSimpleResponseDto> findByTagId(Long tagId) {
        return postRepository.findByTagId(tagId)
                .stream()
                .map(PostSimpleResponseDto::new)
                .toList();
    }

    /** 최신 게시글 목록 조회 */
    public List<PostSimpleResponseDto> findLatestPosts() {
        return postRepository.findLatestPosts()
                .stream()
                .map(PostSimpleResponseDto::new)
                .toList();
    }

}
