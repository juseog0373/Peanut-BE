package com.daelim.hackathon.handler;

import com.daelim.hackathon.domain.ChatRoom;
import com.daelim.hackathon.dto.ChatDto;
import com.daelim.hackathon.service.ChatService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info(session + " 클라이언트 접속");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        try {
            ChatDto chatMessage = objectMapper.readValue(payload, ChatDto.class);

            log.info("session {}", chatMessage.toString());

            ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
            log.info("room {}", room.toString());

            if (session.isOpen()) {
                System.out.println("WebSocketChatHandler.handleTextMessage");
                room.handleAction(session, chatMessage, chatService);
            } else {
                log.warn("Session is closed, cannot handle message");
            }
        } catch (Exception e) {
            log.error("Failed to handle message", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info(session + " 클라이언트 접속 해제");
    }
}
