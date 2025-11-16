package com.loglab.livlog.statistics.postlike.service;

import com.loglab.livlog.post.entity.Post;
import com.loglab.livlog.post.repository.PostRepository;
import com.loglab.livlog.postlike.entity.PostLike;
import com.loglab.livlog.postlike.entity.PostLikeId;
import com.loglab.livlog.statistics.postlike.dto.request.PostLikeRequest;
import com.loglab.livlog.statistics.postlike.dto.response.PostLikeCountResponse;
import com.loglab.livlog.statistics.postlike.dto.response.PostLikeRankingResponse;
import com.loglab.livlog.statistics.postlike.repository.PostLikeRepository;
import com.loglab.livlog.user.entity.User;
import com.loglab.livlog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 관리자 대시보드에서 사용하는 특정 게시글의 좋아요(Like) 데이터를 제공하는 Service
 *
 * @author : 000flag
 * @fileName : PostLikeService
 * @since : 2025.11.14
 */
@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 좋아요 추가
     *
     * 동작:
     *   1) postId와 userId로 게시글, 사용자 엔티티 조회
     *   2) PostLikeId(복합키) 생성
     *   3) 이미 좋아요를 누른 상태가 아닌 경우에만 저장
     *
     * 예외:
     *   - 게시글 또는 사용자가 없으면 orElseThrow()로 예외 발생
     *
     * @param request postId, userId 요청 DTO
     */
    public void addLike(PostLikeRequest request) {
        Post post = postRepository.findById(request.getPostId()).orElseThrow();
        User user = userRepository.findById(request.getUserId()).orElseThrow();

        PostLikeId id = new PostLikeId(request.getPostId(), request.getUserId());

        // 중복 좋아요 방지
        if (!postLikeRepository.existsById(id)) {
            postLikeRepository.save(
                    PostLike.builder()
                            .id(id)
                            .post(post)
                            .user(user)
                            .build()
            );
        }
    }

    /**
     * 좋아요 취소
     *
     * 동작:
     *   - postId + userId 조합으로 좋아요 존재 시 삭제
     *
     * @param request postId, userId 요청 DTO
     */
    public void removeLike(PostLikeRequest request) {
        PostLikeId id = new PostLikeId(request.getPostId(), request.getUserId());

        if (postLikeRepository.existsById(id)) {
            postLikeRepository.deleteById(id);
        }
    }

    /**
     * 특정 게시글의 좋아요 수 조회
     *
     * 사용처:
     *   - 게시글 상세 페이지
     *   - 관리자 대시보드
     *
     * @param postId 게시글 ID
     * @return PostLikeCountResponse(postId, count)
     */
    public PostLikeCountResponse getLikeCount(Long postId) {
        long count = postLikeRepository.countByPost_Id(postId);
        return new PostLikeCountResponse(postId, count);
    }

    /**
     * 특정 사용자가 좋아요한 게시글 ID 목록 조회
     *
     * 예:
     *   - 즐겨찾기 화면 표시
     *   - 사용자 활동 분석
     *
     * @param userId 사용자 ID
     * @return List<Long> (게시글 ID 목록)
     */
    public List<Long> getUserLikedPosts(Long userId) {
        return postLikeRepository.findByUser_Id(userId)
                .stream()
                .map(pl -> pl.getPost().getId())
                .toList();
    }

    /**
     * 좋아요 수 기준 인기 게시글 TOP N 조회
     *
     * JPQL 결과 매핑:
     *   v[0] → postId (Long)
     *   v[1] → postTitle (String)
     *   v[2] → likeCount (Long)
     *
     * 사용처:
     *   - 관리자 대시보드 인기 게시글
     *   - 메인 페이지 추천글
     *
     * @param top 조회할 개수 (예: top=5 → 인기글 5개)
     * @return 인기글 랭킹 리스트
     */
    public List<PostLikeRankingResponse> getTopPosts(int top) {
        return postLikeRepository.getTopLikedPosts()
                .stream()
                .limit(top)
                .map(v -> new PostLikeRankingResponse(
                        (Long) v[0],   // postId
                        (String) v[1], // postTitle
                        (Long) v[2]    // likeCount
                ))
                .toList();
    }
}
