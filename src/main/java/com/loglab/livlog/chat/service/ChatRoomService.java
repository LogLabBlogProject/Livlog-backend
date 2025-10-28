package com.loglab.livlog.chat.service;

import com.loglab.livlog.chat.entity.*;
import com.loglab.livlog.chat.dto.response.ChatRoomDto;
import com.loglab.livlog.chat.dto.response.ChatParticipantDto;
import com.loglab.livlog.chat.repository.ChatParticipantRepository;
import com.loglab.livlog.chat.repository.ChatRoomRepository;
import com.loglab.livlog.chat.mapper.ChatRoomMapper;
import com.loglab.livlog.chat.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final UserService userService;

    /**
     * 1:1 개인 채팅방 생성 or 조회
     */
    @Transactional
    public ChatRoomDto createOrGetPrivateRoom(Long userA, Long userB) {
        // 입력값 검증
        validateUsers(userA, userB);
        
        // (1) 정렬해서 고유 key로 사용
        Long small = Math.min(userA, userB);
        Long big = Math.max(userA, userB);
        String title = small + "_" + big;

        // (2) 기존 방 확인
        Optional<ChatRoom> existing = chatRoomRepository.findByTypeAndTitle(ChatRoom.ChatRoomType.PRIVATE, title);
        if (existing.isPresent()) {
            log.info("기존 1:1 채팅방 조회: {}", title);
            return chatRoomMapper.toDto(existing.get());
        }

        // (3) 새 방 생성
        ChatRoom newRoom = ChatRoom.builder()
                .type(ChatRoom.ChatRoomType.PRIVATE)
                .title(title)
                .createdAt(Instant.now())
                .build();
        chatRoomRepository.save(newRoom);

        // (4) 참여자 2명 등록
        addParticipantToRoom(newRoom, userA);
        addParticipantToRoom(newRoom, userB);

        log.info("새로운 1:1 채팅방 생성: {}", title);
        return chatRoomMapper.toDto(newRoom);
    }

    /**
     * 그룹 채팅방 생성
     */
    @Transactional
    public ChatRoomDto createGroupRoom(String title, List<Long> memberIds) {
        // 입력값 검증
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("채팅방 제목은 필수입니다.");
        }
        if (memberIds == null || memberIds.isEmpty()) {
            throw new IllegalArgumentException("참여자 목록은 필수입니다.");
        }
        
        // 사용자 존재 여부 검증
        for (Long memberId : memberIds) {
            validateUserExists(memberId);
        }

        ChatRoom room = ChatRoom.builder()
                .type(ChatRoom.ChatRoomType.GROUP)
                .title(title.trim())
                .createdAt(Instant.now())
                .build();
        chatRoomRepository.save(room);

        // 참여자들 추가
        for (Long memberId : memberIds) {
            addParticipantToRoom(room, memberId);
        }

        log.info("새로운 그룹 채팅방 생성: {} (참여자 수: {})", title, memberIds.size());
        return chatRoomMapper.toDto(room);
    }

    /**
     * 방에 참여자 추가
     */
    @Transactional
    public Long joinRoom(Long roomId, Long memberId) {
        // 채팅방 존재 여부 확인
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException(roomId));
        
        // 사용자 존재 여부 확인
        validateUserExists(memberId);
        
        // 채팅방 상태 확인
        validateRoomStatus(room);
        
        // 참여자 수 제한 확인
        validateParticipantLimit(room);
        
        // 이미 참여 중인지 확인
        boolean alreadyJoined = chatParticipantRepository.existsByRoomIdAndMemberId(roomId, memberId);
        if (alreadyJoined) {
            throw new DuplicateParticipantException(roomId, memberId);
        }
        
        ChatParticipant participant = addParticipantToRoom(room, memberId);
        log.info("사용자 {}가 채팅방 {}에 참여했습니다.", memberId, roomId);
        return participant.getId();
    }

    /**
     * 내가 참여중인 모든 방 목록 조회 (최적화된 쿼리 사용)
     */
    public List<ChatRoomDto> findRoomsByMember(Long memberId) {
        validateUserExists(memberId);
        
        List<ChatParticipant> myParticipations = chatParticipantRepository.findByMemberIdWithRoom(memberId);
        
        return myParticipations.stream()
                .map(p -> chatRoomMapper.toDto(p.getRoom()))
                .collect(Collectors.toList());
    }


    /**
     * 채팅방 ID로 조회
     */
    public ChatRoom findById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException(roomId));
    }
    
    /**
     * 채팅방 ID로 DTO 조회
     */
    public ChatRoomDto findDtoById(Long roomId) {
        ChatRoom room = findById(roomId);
        return chatRoomMapper.toDto(room);
    }
    
    /**
     * 채팅방 나가기
     */
    @Transactional
    public Long leaveRoom(Long roomId, Long memberId) {
        validateUserExists(memberId);
        
        // 참여 여부 확인
        Optional<ChatParticipant> participant = chatParticipantRepository.findByRoomIdAndMemberId(roomId, memberId);
        if (participant.isEmpty()) {
            throw new IllegalArgumentException("참여하지 않은 채팅방입니다.");
        }
        Long participantId = participant.get().getId();
        chatParticipantRepository.deleteByRoomIdAndMemberId(roomId, memberId);
        log.info("사용자 {}가 채팅방 {}에서 나갔습니다.", memberId, roomId);
        return participantId;
    }
    
    // === Private Helper Methods ===
    
    /**
     * 사용자 존재 여부 검증
     */
    private void validateUserExists(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
        }
        
        // TODO: 실제 구현에서는 UserService를 통해 검증
        // userService.findUserById(userId)
        //         .orElseThrow(() -> new UserNotFoundException(userId));
        
        // 현재는 기본적인 ID 유효성만 검증
        // 실제 운영 환경에서는 반드시 UserService 연동 필요
    }
    
    /**
     * 두 사용자 검증 (1:1 채팅용)
     */
    private void validateUsers(Long userA, Long userB) {
        if (userA == null || userB == null) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다.");
        }
        if (userA.equals(userB)) {
            throw new IllegalArgumentException("자기 자신과는 1:1 채팅을 할 수 없습니다.");
        }
        
        validateUserExists(userA);
        validateUserExists(userB);
    }
    
    /**
     * 채팅방에 참여자 추가 (공통 로직)
     */
    private ChatParticipant addParticipantToRoom(ChatRoom room, Long memberId) {
        // 사용자 정보 조회 (실제 구현에서는 UserService 사용)
        String memberName = "user_" + memberId; // 임시 구현
        
        ChatParticipant participant = ChatParticipant.builder()
                .room(room)
                .memberId(memberId)
                .memberName(memberName)
                .joinedAt(Instant.now())
                .build();
        
        return chatParticipantRepository.save(participant);
    }
    
    /**
     * 채팅방 상태 검증
     */
    private void validateRoomStatus(ChatRoom room) {
        // TODO: 실제 구현에서는 room.getStatus() 등을 확인
        // if (room.getStatus() == ChatRoomStatus.INACTIVE) {
        //     throw new ChatRoomInactiveException(room.getId());
        // }
    }
    
    /**
     * 참여자 수 제한 검증
     */
    private void validateParticipantLimit(ChatRoom room) {
        int currentCount = room.getParticipants().size();
        int maxLimit = getMaxParticipantLimit(room.getType());
        
        if (currentCount >= maxLimit) {
            throw new ChatParticipantLimitExceededException(room.getId(), currentCount, maxLimit);
        }
    }
    
    /**
     * 채팅방 타입별 최대 참여자 수 제한
     */
    private int getMaxParticipantLimit(ChatRoom.ChatRoomType type) {
        return switch (type) {
            case PRIVATE -> 2;
            case GROUP -> 50; // 그룹 채팅방 최대 50명
        };
    }

}