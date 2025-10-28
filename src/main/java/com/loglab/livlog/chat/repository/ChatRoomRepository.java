package com.loglab.livlog.chat.repository;

import com.loglab.livlog.chat.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByTypeAndTitle(ChatRoom.ChatRoomType type, String title);
}

