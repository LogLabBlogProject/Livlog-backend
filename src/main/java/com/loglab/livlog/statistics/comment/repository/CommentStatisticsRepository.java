package com.loglab.livlog.statistics.comment.repository;

import com.loglab.livlog.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 관리자 대시보드의 댓글(Comment) 통계를 조회하기 위한 Repository
 *
 * @author : 000flag
 * @fileName : CommentStatisticsRepository
 * @since : 2025.11.13
 */
@Repository
public interface CommentStatisticsRepository extends JpaRepository<Comment, Long> {
    /**
     * 전체 댓글 수 조회
     *
     * @return 전체 댓글 레코드 수
     */
    long count();

    /**
     * 작성자별 댓글 수를 집계하여 TOP N 순으로 정렬하는 쿼리
     *
     * Query 구조:
     *   SELECT c.user.id, c.user.username, COUNT(c)
     *   FROM Comment c
     *   WHERE c.user.id IS NOT NULL
     *   GROUP BY c.user.id, c.user.username
     *   ORDER BY COUNT(c) DESC
     *
     * 반환 Object[]:
     *   - [0] Long   : userId
     *   - [1] String : username
     *   - [2] Long   : commentCount
     *
     * @return 작성자별 댓글 수 통계 리스트
     */
    @Query("""
            SELECT c.user.id, c.user.username, COUNT(c)
            FROM Comment c
            WHERE c.user.id IS NOT NULL
            GROUP BY c.user.id, c.user.username
            ORDER BY COUNT(c) DESC
            """)
    List<Object[]> getTopCommentWriters();

    /**
     * 포스트(post)별 댓글 수를 집계하여 TOP N 순으로 정렬하는 쿼리
     *
     * Query 구조:
     *   SELECT c.post.id, c.post.title, COUNT(c)
     *   FROM Comment c
     *   WHERE c.post.id IS NOT NULL
     *   GROUP BY c.post.id, c.post.title
     *   ORDER BY COUNT(c) DESC
     *
     * 반환 Object[]:
     *   - [0] Long   : postId
     *   - [1] String : postTitle
     *   - [2] Long   : commentCount
     *
     * @return 포스트별 댓글 수 통계 리스트
     */
    @Query("""
            SELECT c.post.id, c.post.title, COUNT(c)
            FROM Comment c
            WHERE c.post.id IS NOT NULL
            GROUP BY c.post.id, c.post.title
            ORDER BY COUNT(c) DESC
            """)
    List<Object[]> getPostCommentRanking();
}
