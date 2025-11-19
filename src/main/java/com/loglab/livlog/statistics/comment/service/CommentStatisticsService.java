package com.loglab.livlog.statistics.comment.service;

import com.loglab.livlog.statistics.comment.dto.response.CommentBasicStatsResponse;
import com.loglab.livlog.statistics.comment.dto.response.CommentWriterTopResponse;
import com.loglab.livlog.statistics.comment.dto.response.PostCommentRankingResponse;
import com.loglab.livlog.statistics.comment.repository.CommentStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 관리자 대시보드에서 사용하는 댓글(Comment) 통계 데이터를 제공하는 Service
 *
 * @author : 000flag
 * @fileName : CommentStatisticsService
 * @since : 2025.11.13
 */
@Service
@RequiredArgsConstructor
public class CommentStatisticsService {
    private final CommentStatisticsRepository commentStatisticsRepository;

    /**
     * 전체 댓글 수를 조회하여 반환
     *
     * Repository → count() 사용.
     *
     * @return CommentBasicStatsResponse 전체 댓글 수 DTO
     */
    public CommentBasicStatsResponse getBasicStats() {
        return new CommentBasicStatsResponse(commentStatisticsRepository.count());
    }

    /**
     * 작성자별 댓글 수를 기준으로 TOP N을 조회
     *
     * Query 반환값(Object[]) 구조:
     *   [0] Long   : userId
     *   [1] String : username
     *   [2] Long   : commentCount
     *
     * Stream 처리 후 CommentWriterTopResponse DTO로 변환하여 반환한다.
     *
     * @param top 조회할 TOP N 값
     * @return List<CommentWriterTopResponse> 작성자 순위 리스트
     */
    public List<CommentWriterTopResponse> getTopWriters(int top) {
        return commentStatisticsRepository.getTopCommentWriters()
                .stream()
                .limit(top)
                .map(v -> new CommentWriterTopResponse(
                        (Long) v[0],   // userId
                        (String) v[1], // username
                        (Long) v[2]    // 댓글 개수
                ))
                .toList();
    }

    /**
     * 포스트별 댓글 수를 집계하여 TOP N을 조회
     *
     * Query 반환값(Object[]) 구조:
     *   [0] Long   : postId
     *   [1] String : postTitle
     *   [2] Long   : commentCount
     *
     * Stream 처리 후 PostCommentRankingResponse DTO로 변환.
     *
     * @param top 조회할 TOP N 값
     * @return List<PostCommentRankingResponse> 포스트별 댓글 순위 리스트
     */
    public List<PostCommentRankingResponse> getPostRanking(int top) {
        return commentStatisticsRepository.getPostCommentRanking()
                .stream()
                .limit(top)
                .map(v -> new PostCommentRankingResponse(
                        (Long) v[0],   // postId
                        (String) v[1], // postTitle
                        (Long) v[2]    // 댓글 개수
                ))
                .toList();
    }
}
