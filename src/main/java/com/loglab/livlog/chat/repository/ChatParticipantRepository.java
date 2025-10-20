package com.loglab.livlog.chat.repository;

import com.loglab.livlog.chat.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    List<ChatParticipant> findByRoomId(Long roomId);
    boolean existsByRoomIdAndMemberId(Long roomId, Long memberId);
}
