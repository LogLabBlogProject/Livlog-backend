package com.loglab.livlog.statistics.post.service;

import com.loglab.livlog.post.entity.Post;
import com.loglab.livlog.statistics.post.dto.response.DailyPostCountResponse;
import com.loglab.livlog.statistics.post.dto.response.PostBasicStatsResponse;
import com.loglab.livlog.statistics.post.dto.response.WriterPostCountResponse;
import com.loglab.livlog.statistics.post.repository.PostStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 관리자 대시보드에서 사용하는 포스트(post) 통계 데이터를 제공하는 Service
 *
 * @author : 000flag
 * @fileName : PostStatisticsService
 * @since : 2025.11.11
 */
@Service
@RequiredArgsConstructor
public class PostStatisticsService {
    private final PostStatisticsRepository postStatisticsRepository;

    /**
     * 포스트 기본 통계 조회
     *
     * 조회 항목:
     *   - totalPosts      : 전체 포스트 수
     *   - postsInRange    : 기간 내 작성된 포스트 수
     *   - deletedPosts    : soft delete 된 포스트 수
     *   - averageWordCount: 포스트 평균 단어 수
     *
     * @param start 시작 날짜/시간
     * @param end   종료 날짜/시간
     * @return PostBasicStatsResponse 기본 통계 DTO
     */
    public PostBasicStatsResponse getBasicStats(LocalDateTime start, LocalDateTime end) {
        long totalPosts = postStatisticsRepository.count();
        long postsInRange = postStatisticsRepository.countByCreatedAtBetween(start, end);
        long deletedPosts = postStatisticsRepository.countByDeletedAtIsNotNull();

        double avgWordCount = calcAverageWordCount();

        return PostBasicStatsResponse.builder()
                .totalPosts(totalPosts)
                .postsInRange(postsInRange)
                .deletedPosts(deletedPosts)
                .averageWordCount(avgWordCount)
                .build();
    }

    /**
     * 전체 포스트의 평균 글 길이(단어 수)를 계산
     *
     * 기준:
     *   - content 컬럼의 단어 수를 공백 기준 split
     *   - null content 는 제외
     *
     * @return 평균 단어 수
     */
    private double calcAverageWordCount() {
        List<Post> posts = postStatisticsRepository.findAll();
        if (posts.isEmpty()) return 0;

        double totalWords = posts.stream()
                .filter(p -> p.getContent() != null)
                .mapToInt(p -> p.getContent().split("\\s+").length)
                .sum();

        return totalWords / posts.size();
    }

    /**
     * 특정 기간(start ~ end) 동안 작성된 포스트 수를 일자별로 집계하여 반환
     *
     * 사용 위치:
     *   - 관리자 대시보드 그래프(Line chart, Bar chart)
     *
     * Repository 반환:
     *   Object[]:
     *     - [0] 작성일(Date)
     *     - [1] 작성 수(Long)
     *
     * @param start 조회 시작일
     * @param end   조회 종료일
     * @return DailyPostCountResponse 리스트
     */
    public List<DailyPostCountResponse> getDailyPosts(LocalDateTime start, LocalDateTime end) {
        return postStatisticsRepository.getDailyPostStats(start, end)
                .stream()
                .map(v -> new DailyPostCountResponse(
                        v[0].toString(),   // 날짜 정보
                        (Long) v[1]        // 해당 날짜의 글 수
                ))
                .toList();
    }

    /**
     * 작성자별 포스트 작성 수를 집계하여 상위 10명을 반환
     *
     * Repository 반환:
     *   Object[]
     *     - [0] userId(Long)
     *     - [1] username(String)
     *     - [2] postCount(Long)
     *
     * @return WriterPostCountResponse 리스트 (TOP 10)
     */
    public List<WriterPostCountResponse> getTopWriters() {
        return postStatisticsRepository.getTopWriters()
                .stream()
                .limit(10) // TOP 10 제한
                .map(v -> new WriterPostCountResponse(
                        (Long) v[0],   // userId
                        (String) v[1], // username
                        (Long) v[2]    // postCount
                ))
                .toList();
    }
}
