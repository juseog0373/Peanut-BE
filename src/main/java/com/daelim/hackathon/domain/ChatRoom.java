package com.daelim.hackathon.domain;

import com.daelim.hackathon.dto.ChatDto;
import com.daelim.hackathon.service.ChatService;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Data
public class ChatRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handleAction(WebSocketSession session, ChatDto chatDto, ChatService chatService) {
        if ("ENTER".equals(chatDto.getType())) {
            sessions.add(session);
            System.out.println("session = " + session);
            chatDto.setMessage(chatDto.getSender() + "님이 입장했습니다.");
            sendMessage(chatDto, chatService);
        }
        else if ("TALK".equals(chatDto.getType())) {
            chatDto.setMessage(chatDto.getMessage());
            sendMessage(chatDto, chatService);
        }
    }
    public <T> void sendMessage(T message, ChatService service) {
        sessions.parallelStream().forEach(session -> service.sendMessage(session, message));
    }

    public int getParticipantCount() {
        return sessions.size();
    }
}
