package com.loglab.livlog.chat.controller;

import com.loglab.livlog.chat.dto.request.GroupRoomCreateRequestDto;
import com.loglab.livlog.chat.dto.response.ChatRoomDto;
import com.loglab.livlog.chat.service.ChatRoomService;
import com.loglab.livlog.global.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatRestController {

    private final ChatRoomService chatRoomService;

    /**
     * 1:1 개인채팅방 생성 또는 기존 방 조회
     */
    @PostMapping("/rooms/private")
    public CommonResponse<ChatRoomDto> createPrivateRoom(
            @RequestParam Long otherUserId, 
            Principal principal) {
        Long me = Long.parseLong(principal.getName());
        ChatRoomDto room = chatRoomService.createOrGetPrivateRoom(me, otherUserId);
        return CommonResponse.success("1:1 채팅방 조회/생성 완료", room);
    }

    /**
     * 그룹채팅방 생성
     */
    @PostMapping("/rooms/group")
    public CommonResponse<ChatRoomDto> createGroupRoom(
            @Valid @RequestBody GroupRoomCreateRequestDto req) {
        ChatRoomDto room = chatRoomService.createGroupRoom(req.getTitle(), req.getMemberIds());
        return CommonResponse.success("그룹 채팅방 생성 완료", room);
    }

    /**
     * 채팅방에 참여자 추가
     */
    @PostMapping("/rooms/{roomId}/participants")
    public CommonResponse<Long> addParticipant(
            @PathVariable Long roomId, 
            @RequestParam Long memberId) {
        Long participantId = chatRoomService.joinRoom(roomId, memberId);
        return CommonResponse.success("채팅방 참여 완료", participantId);
    }

    /**
     * 내 채팅방 목록 조회
     */
    @GetMapping("/my/rooms")
    public CommonResponse<List<ChatRoomDto>> getMyRooms(Principal principal) {
        Long me = Long.parseLong(principal.getName());
        List<ChatRoomDto> rooms = chatRoomService.findRoomsByMember(me);
        return CommonResponse.success("채팅방 목록 조회 완료", rooms);
    }

    /**
     * 채팅방 나가기
     */
    @DeleteMapping("/rooms/{roomId}/participants/me")
    public CommonResponse<Long> leaveRoom(
            @PathVariable Long roomId, 
            Principal principal) {
        Long me = Long.parseLong(principal.getName());
        Long participantId = chatRoomService.leaveRoom(roomId, me);
        return CommonResponse.success("채팅방 나가기 완료", participantId);
    }

    /**
     * 채팅방 상세 조회 (참여자 목록 포함)
     */
    @GetMapping("/rooms/{roomId}")
    public CommonResponse<ChatRoomDto> getRoom(@PathVariable Long roomId) {
        ChatRoomDto room = chatRoomService.findDtoById(roomId);
        return CommonResponse.success("채팅방 상세 조회 완료", room);
    }
}
