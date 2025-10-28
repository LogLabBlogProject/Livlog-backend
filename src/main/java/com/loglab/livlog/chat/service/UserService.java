package com.loglab.livlog.chat.service;

import java.util.Optional;

/**
 * 사용자 정보 조회를 위한 서비스 인터페이스
 * 실제 구현은 user 모듈에서 제공
 */
public interface UserService {
    
    /**
     * 사용자 ID로 사용자 정보 조회
     * @param userId 사용자 ID
     * @return 사용자 정보 (존재하지 않으면 Optional.empty())
     */
    Optional<UserInfo> findUserById(Long userId);
    
    /**
     * 사용자 정보 DTO
     */
    record UserInfo(Long id, String username, String profileImageUrl) {}
}
