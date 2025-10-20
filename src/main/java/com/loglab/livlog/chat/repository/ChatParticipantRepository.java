package com.loglab.livlog.chat.repository;

import com.loglab.livlog.chat.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    List<ChatParticipant> findByRoomId(Long roomId);
    boolean existsByRoomIdAndMemberId(Long roomId, Long memberId);
    
    // 특정 사용자가 참여한 모든 채팅방 조회 (최적화된 쿼리)
    @Query("SELECT p FROM ChatParticipant p JOIN FETCH p.room WHERE p.memberId = :memberId")
    List<ChatParticipant> findByMemberIdWithRoom(@Param("memberId") Long memberId);
    
    // 특정 사용자와 다른 사용자 간의 1:1 채팅방 조회
    @Query("SELECT p FROM ChatParticipant p JOIN FETCH p.room WHERE p.room.type = 'PRIVATE' AND p.memberId = :memberId")
    List<ChatParticipant> findPrivateRoomsByMemberId(@Param("memberId") Long memberId);
    
    // 특정 사용자가 특정 채팅방에 참여하고 있는지 확인
    Optional<ChatParticipant> findByRoomIdAndMemberId(Long roomId, Long memberId);
    
    // 채팅방에서 사용자 제거
    void deleteByRoomIdAndMemberId(Long roomId, Long memberId);
}
