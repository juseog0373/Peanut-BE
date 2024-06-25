package com.daelim.hackathon.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatDto {
    private String type; // 메세지타입(ENTER: 방 입장, TALK: 메세지 보내기)
    private String roomId; // 방 번호(UUID) 랜덤 생성
    private String sender; // 채팅을 보낸 사람
    private String message; // 메세지
    private String time; // 발송 시간
}
