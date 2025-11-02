package com.loglab.livlog.bloginfo.repository;

import com.loglab.livlog.bloginfo.entity.BlogInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogInfoRepository extends JpaRepository<BlogInfo, Long> {
    Optional<BlogInfo> findByUserId(Long userId);
}
