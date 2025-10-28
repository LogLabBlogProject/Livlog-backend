package com.loglab.livlog.post.repository;

import com.loglab.livlog.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // JPQL 검색 조회 메서드
    @Query("SELECT p FROM Post p " +
            "WHERE (:title IS NULL OR LOWER(p.title) LIKE CONCAT('%', LOWER(:title), '%')) " +
            "AND (:content IS NULL OR LOWER(p.content) LIKE CONCAT('%', LOWER(:content), '%')) " +
            "AND (:sCreatedAt IS NULL OR p.createdAt >= :sCreatedAt) " +
            "AND (:eCreatedAt IS NULL OR p.createdAt <= :eCreatedAt) " +
            "AND (:sUpdatedAt IS NULL OR p.updatedAt >= :sUpdatedAt) " +
            "AND (:eUpdatedAt IS NULL OR p.updatedAt <= :eUpdatedAt)")
    List<Post> searchBy(@Param("title") String title,
                        @Param("content") String content,
                        @Param("sCreatedAt") LocalDateTime sCreatedAt,
                        @Param("eCreatedAt") LocalDateTime eCreatedAt,
                        @Param("sUpdatedAt") LocalDateTime sUpdatedAt,
                        @Param("eUpdatedAt") LocalDateTime eUpdatedAt);

    List<Post> findByUserId(Long userId);
}
