package com.loglab.livlog.statistics.post.repository;

import com.loglab.livlog.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 포스트(post) 테이블을 기반으로 관리자 대시보드 통계 데이터를 조회하는 Repository
 *
 * @author : 000flag
 * @fileName : PostStatisticsRepository
 * @since : 2025.11.11
 */
@Repository
public interface PostStatisticsRepository extends JpaRepository<Post, Long> {
    /**
     * 전체 포스트 수 조회
     *
     * @return 모든 포스트 개수
     */
    long count();


    /**
     * 기간(start ~ end) 내 작성된 포스트 수 조회
     *
     * @param start 조회 시작 날짜/시간
     * @param end   조회 종료 날짜/시간
     * @return 해당 기간에 작성된 포스트 수
     */
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);


    /**
     * deleted_at 이 NOT NULL 인 soft-deleted 포스트 수 조회
     *
     * @return 삭제 처리된 포스트 개수
     */
    long countByDeletedAtIsNotNull();


    /**
     * 일별 포스트 작성 건수를 조회하는 쿼리
     *
     * SELECT DATE(p.createdAt), COUNT(p)
     *   FROM Post p
     *  WHERE p.createdAt BETWEEN :start AND :end
     *  GROUP BY DATE(p.createdAt)
     *
     * @param start 조회 시작일
     * @param end   조회 종료일
     * @return Object[] 배열 리스트
     *         - [0] String: 날짜
     *         - [1] Long  : 작성된 포스트 수
     */
    @Query("SELECT DATE(p.createdAt), COUNT(p) " +
            "FROM Post p " +
            "WHERE p.createdAt BETWEEN :start AND :end " +
            "GROUP BY DATE(p.createdAt)")
    List<Object[]> getDailyPostStats(LocalDateTime start, LocalDateTime end);


    /**
     * 작성자별 포스트 작성 수를 조회하는 쿼리
     *
     * SELECT p.user.id, p.user.username, COUNT(p)
     *   FROM Post p
     *  GROUP BY p.user.id, p.user.username
     *  ORDER BY COUNT(p) DESC
     *
     * 서비스에서 TOP10으로 제한하여 반환
     *
     * @return Object[] 배열 리스트
     *         - [0] Long   : 사용자 ID
     *         - [1] String : 사용자명(username)
     *         - [2] Long   : 작성 포스트 수
     */
    @Query("SELECT p.user.id, p.user.username, COUNT(p) " +
            "FROM Post p " +
            "GROUP BY p.user.id, p.user.username " +
            "ORDER BY COUNT(p) DESC")
    List<Object[]> getTopWriters();
}
