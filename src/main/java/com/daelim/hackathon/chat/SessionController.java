package com.daelim.hackathon.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/room")
public class SessionController {

    private final ChatService chatService;

    @GetMapping("/id")
    public String getSessionId(@RequestHeader("session-id") String sessionId) {
        Optional<WebSocketSession> session = chatService.getSessionById(sessionId);
        return session.map(WebSocketSession::getId).orElse("Session not found");
    }
}
