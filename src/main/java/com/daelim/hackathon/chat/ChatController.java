package com.daelim.hackathon.chat;

import com.daelim.hackathon.chat.dto.ChatRoomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        log.info("Creating chat room with name: {}", name);
        ChatRoom chatRoom = chatService.createRoom(name);
        return new ChatRoomResponse(chatRoom.getRoomId(), chatRoom.getName(), chatRoom.getParticipantCount(), chatService.getTotalActiveUsers(), chatRoom.getParticipants());
    }

    @GetMapping("/rooms")
    public List<ChatRoomResponse> findAllRooms() {
        log.info("Retrieving all chat rooms");
        List<ChatRoom> chatRooms = chatService.findAllRoom();
        log.info("Found {} rooms", chatRooms.size());
        return chatRooms.stream()
                .map(room -> new ChatRoomResponse(room.getRoomId(), room.getName(), room.getParticipantCount(), chatService.getTotalActiveUsers(), room.getParticipants()))
                .toList();
    }

    @GetMapping("/room/{id}")
    public ChatRoomResponse findRoomById(@PathVariable String id) {
        ChatRoom chatRoom = chatService.findRoomById(id);
        return new ChatRoomResponse(chatRoom.getRoomId(), chatRoom.getName(), chatRoom.getParticipantCount(), chatService.getTotalActiveUsers(), chatRoom.getParticipants());
    }

    @GetMapping("/{roomId}/isUserInRoom")
    public boolean isUserInRoom(@PathVariable String roomId, @RequestHeader("session-id") String sessionId) {
        log.info("Checking if session {} is in room {}", sessionId, roomId);
        boolean result = chatService.isUserInRoom(roomId, sessionId);
        log.info("Session {} in room {}: {}", sessionId, roomId, result);
        return result;
    }
}