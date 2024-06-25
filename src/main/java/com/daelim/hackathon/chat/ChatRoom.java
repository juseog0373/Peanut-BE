package com.daelim.hackathon.chat;

import com.daelim.hackathon.chat.dto.ChatDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ChatRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions;

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
        this.sessions = new HashSet<>();
    }

    public void handleAction(WebSocketSession session, ChatDto chatDto, ChatService chatService) {
        if ("ENTER".equals(chatDto.getType())) {
            sessions.add(session);
            chatDto.setMessage(chatDto.getSender() + "님이 입장했습니다.");
            sendMessage(chatDto, chatService);
        } else if ("TALK".equals(chatDto.getType())) {
            chatDto.setMessage(chatDto.getMessage());
            sendMessage(chatDto, chatService);
        }
    }

    public <T> void sendMessage(T message, ChatService service) {
        sessions.parallelStream().forEach(session -> service.sendMessage(session, message));
    }

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public int getParticipantCount() {
        return sessions.size();
    }

    public Set<String> getParticipants() {
        return sessions.stream()
                .map(WebSocketSession::getId)
                .collect(Collectors.toSet());
    }
}
