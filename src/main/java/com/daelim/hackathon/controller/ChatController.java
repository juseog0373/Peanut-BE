package com.daelim.hackathon.controller;

import com.daelim.hackathon.domain.ChatRoom;
import com.daelim.hackathon.dto.ChatRoomResponse;
import com.daelim.hackathon.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/room")
    public ChatRoomResponse createRoom(@RequestParam String name) {
        log.info("채팅방 생성 완료, 방 이름: {}", name);
        ChatRoom chatRoom = chatService.creatRoom(name);
        return new ChatRoomResponse(chatRoom.getRoomId(), chatRoom.getName(), chatRoom.getParticipantCount());
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> findAllRooms() {
        List<ChatRoomResponse> chatRooms = chatService.findAllRoom().stream().map(room -> new ChatRoomResponse(room.getRoomId(), room.getName(), room.getParticipantCount())).toList();
        log.info("Found {} rooms", chatRooms.size());
        return ResponseEntity.ok().body(chatRooms);
    }

    @GetMapping("/room/{roomId}")
    public ChatRoomResponse findRoomById(@PathVariable String roomId) {
        ChatRoom chatRoom = chatService.findRoomById(roomId);
        return new ChatRoomResponse(chatRoom.getRoomId(), chatRoom.getName(), chatRoom.getParticipantCount());
    }
}