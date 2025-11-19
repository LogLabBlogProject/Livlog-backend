package com.loglab.livlog.statistics.user.repository;

import com.loglab.livlog.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * user 테이블(User 엔터티)을 조회하는 Repository
 *
 * @author : 000flag
 * @fileName : UserRepository
 * @since : 2025.11.05
 */
@Repository
public interface UserStatisticsRepository extends JpaRepository<User, Long> {

    /**
     * 기간(startDate ~ endDate) 동안 날짜별 신규 가입자 수 통계 조회.
     *
     * @param start 조회 시작일
     * @param end   조회 종료일
     * @return Object[] -> [0] 날짜(Date), [1] 가입자 수(Long)
     */
    @Query("SELECT DATE(u.createdAt), COUNT(u) " +
            "FROM User u " +
            "WHERE u.createdAt BETWEEN :start AND :end " +
            "GROUP BY DATE(u.createdAt)")
    List<Object[]> getDailySignUps(LocalDateTime start, LocalDateTime end);


    /**
     * endDate 기준 누적 가입자 수 조회.
     *
     * @param end 조회 종료일
     * @return 누적 가입자 수
     */
    long countByCreatedAtLessThanEqual(LocalDateTime end);
}
