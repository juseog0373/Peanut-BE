package com.daelim.hackathon.chat;

import com.daelim.hackathon.chat.ChatRoom;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Slf4j
@Data
@Service
public class ChatService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, ChatRoom> chatRooms;
    private Set<WebSocketSession> allSessions;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
        allSessions = new HashSet<>();
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(String name) {
        String roomId = UUID.randomUUID().toString();

        ChatRoom room = ChatRoom.builder()
                .roomId(roomId)
                .name(name)
                .build();
        chatRooms.put(roomId, room);

        return room;
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void addSession(WebSocketSession session) {
        allSessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        allSessions.remove(session);
    }

    public void addSessionToRoom(String roomId, WebSocketSession session) {
        ChatRoom room = chatRooms.get(roomId);
        if (room != null) {
            room.addSession(session);
        }
        allSessions.add(session);
    }

    public Optional<WebSocketSession> getSessionById(String sessionId) {
        return allSessions.stream()
                .filter(session -> session.getId().equals(sessionId))
                .findFirst();
    }

    public void removeSessionFromRoom(String roomId, WebSocketSession session) {
        ChatRoom room = chatRooms.get(roomId);
        if (room != null) {
            room.removeSession(session);
        }
        allSessions.remove(session);
    }

    public int getTotalActiveUsers() {
        return allSessions.size();
    }

    public boolean isUserInRoom(String roomId, String sessionId) {
        ChatRoom room = chatRooms.get(roomId);
        if (room != null) {
            return room.getParticipants().contains(sessionId);
        }
        return false;
    }
}