package com.daelim.hackathon.handler;

import com.daelim.hackathon.domain.ChatRoom;
import com.daelim.hackathon.dto.ChatDto;
import com.daelim.hackathon.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        try {
            ChatDto chatMessage = objectMapper.readValue(payload, ChatDto.class);
            log.info("session {}", chatMessage.toString());

            chatMessage.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
            log.info("room {}", room.toString());

            if (session.isOpen()) {
                room.handleAction(session, chatMessage, chatService);
                if ("ENTER".equals(chatMessage.getType())) {
                    chatService.addSessionToRoom(chatMessage.getRoomId(), session);
                    session.sendMessage(new TextMessage("My Session ID: " + session.getId()));
                }
            } else {
                log.warn("session closed cannot handle message");
            }
        } catch (Exception e) {
            log.error("Failed handle message: ", e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info(session + " 클라이언트 접속");
        chatService.addSession(session);
        session.sendMessage(new TextMessage("Session ID: " + session.getId())); // 세션 ID 전송
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info(session + " 클라이언트 접속 해제");

        // Room 세션에서 제거
        for (ChatRoom room : chatService.findAllRoom()) {
            if (room.getParticipants().contains(session.getId())) {
                chatService.removeSessionFromRoom(room.getRoomId(), session);
                break;
            }
        }

        // 세션을 allSessions에서 제거
        chatService.removeSession(session);
    }
}
