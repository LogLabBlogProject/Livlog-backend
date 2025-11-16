package com.loglab.livlog.statistics.postlike.repository;

import com.loglab.livlog.postlike.entity.PostLike;
import com.loglab.livlog.postlike.entity.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 관리자 대시보드의 특정 게시글의 좋아요(Like) 통계를 조회하기 위한 Repository
 *
 * @author : 000flag
 * @fileName : PostLikeRepository
 * @since : 2025.11.14
 */
@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    /**
     * 특정 게시글의 좋아요 수 조회
     *
     * SQL 변환 예:
     *   SELECT COUNT(*) FROM post_like WHERE post_id = :postId;
     *
     * @param postId 게시글 ID
     * @return 좋아요 개수
     */
    long countByPost_Id(Long postId);

    /**
     * 특정 사용자가 좋아요를 누른 모든 게시글 목록 조회
     *
     * SQL 변환 예:
     *   SELECT * FROM post_like WHERE user_id = :userId;
     *
     * @param userId 사용자 ID
     * @return PostLike 엔티티 리스트
     */
    List<PostLike> findByUser_Id(Long userId);

    /**
     * 좋아요 수 기준 인기 게시글 TOP N 조회
     *
     * 반환 컬럼:
     *   - index 0: post_id
     *   - index 1: post_title
     *   - index 2: like_count
     *
     * 사용처:
     *   - 관리자 대시보드 인기글 통계
     *   - 메인 인기글 섹션 추천용
     *
     * JPQL:
     *   SELECT pl.post.id, pl.post.title, COUNT(pl)
     *   FROM PostLike pl
     *   GROUP BY pl.post.id, pl.post.title
     *   ORDER BY COUNT(pl) DESC
     *
     * @return 인기글 정렬된 데이터 리스트
     */
    @Query("""
            SELECT pl.post.id, pl.post.title, COUNT(pl)
            FROM PostLike pl
            GROUP BY pl.post.id, pl.post.title
            ORDER BY COUNT(pl) DESC
            """)
    List<Object[]> getTopLikedPosts();

    /**
     * 특정 사용자가 특정 게시글에 좋아요를 눌렀는지 여부 확인
     *
     * SQL 변환 예:
     *   SELECT EXISTS(
     *      SELECT 1 FROM post_like
     *      WHERE post_id = :postId
     *      AND   user_id = :userId
     *   );
     *
     * @param postId 게시글 ID
     * @param userId 사용자 ID
     * @return true = 좋아요를 눌렀음 / false = 누르지 않았음
     */
    boolean existsByPost_IdAndUser_Id(Long postId, Long userId);
}
