package com.daelim.hackathon.controller;

import com.daelim.hackathon.domain.ChatRoom;
import com.daelim.hackathon.dto.ChatRoomResponse;
import com.daelim.hackathon.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/room")
    public ChatRoomResponse createRoom(@RequestParam String name) {
        log.info("Creation room: {}", name);
        ChatRoom chatRoom = chatService.creatRoom(name);
        return new ChatRoomResponse(chatRoom.getRoomId(), chatRoom.getName(), chatRoom.getUserInRoomCount(), chatRoom.getAllUserCount());
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> findAllRooms() {
        List<ChatRoomResponse> chatRooms = chatService.findAllRoom()
                .stream()
                .map(room -> new ChatRoomResponse(room.getRoomId(), room.getName(), room.getUserInRoomCount(), room.getAllUserCount()))
                .toList();
        log.info("Found {} rooms", chatRooms.size());
        return ResponseEntity.ok().body(chatRooms);
    }

    @GetMapping("/room/{roomId}")
    public ChatRoomResponse findRoomById(@PathVariable String roomId) {
        ChatRoom chatRoom = chatService.findRoomById(roomId);
        return new ChatRoomResponse(chatRoom.getRoomId(), chatRoom.getName(), chatRoom.getUserInRoomCount(), chatRoom.getAllUserCount());
    }
}