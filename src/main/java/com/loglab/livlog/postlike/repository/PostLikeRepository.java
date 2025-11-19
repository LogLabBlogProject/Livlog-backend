package com.loglab.livlog.postlike.repository;

import com.loglab.livlog.postlike.entity.PostLike;
import com.loglab.livlog.postlike.entity.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    List<PostLike> findByIdPostId(Long postId);
    List<PostLike> findByIdUserId(Long userId);

    @Query("SELECT COUNT(pl) FROM PostLike pl WHERE pl.id.postId = :postId")
    Long countByPostId(Long postId);

    boolean existsByIdPostIdAndIdUserId(Long postId, Long userId);
}
