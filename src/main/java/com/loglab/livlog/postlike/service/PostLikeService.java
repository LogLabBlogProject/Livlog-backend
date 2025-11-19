package com.loglab.livlog.postlike.service;

import com.loglab.livlog.post.entity.Post;
import com.loglab.livlog.post.repository.PostRepository;
import com.loglab.livlog.postlike.dto.PostLikeDto;
import com.loglab.livlog.postlike.entity.PostLike;
import com.loglab.livlog.postlike.entity.PostLikeId;
import com.loglab.livlog.postlike.repository.PostLikeRepository;
import com.loglab.livlog.user.entity.User;
import com.loglab.livlog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostLikeDto toggleLike(Long postId, Long userId) {
        PostLikeId id = new PostLikeId(postId, userId);

        // 이미 좋아요가 있으면 삭제
        if (postLikeRepository.existsByIdPostIdAndIdUserId(postId, userId)) {
            postLikeRepository.deleteById(id);
            return null; // 좋아요 취소
        }

        // 좋아요 추가
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        PostLike postLike = PostLike.builder()
                .id(id)
                .post(post)
                .user(user)
                .build();

        PostLike saved = postLikeRepository.save(postLike);
        return convertToDto(saved);
    }

    public Long getLikeCount(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }

    public List<PostLikeDto> getLikesByPost(Long postId) {
        List<PostLike> likes = postLikeRepository.findByIdPostId(postId);
        return likes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PostLikeDto> getLikesByUser(Long userId) {
        List<PostLike> likes = postLikeRepository.findByIdUserId(userId);
        return likes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getPostLikeInfo(Long postId, Long userId) {
        Map<String, Object> info = new HashMap<>();
        info.put("likeCount", getLikeCount(postId));
        info.put("isLiked", postLikeRepository.existsByIdPostIdAndIdUserId(postId, userId));
        return info;
    }

    private PostLikeDto convertToDto(PostLike postLike) {
        return PostLikeDto.builder()
                .postId(postLike.getId().getPostId())
                .userId(postLike.getId().getUserId())
                .build();
    }
}
