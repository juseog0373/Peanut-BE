package com.daelim.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class ChatRoomResponse {
    private String roomId; // 채팅방 번호
    private String name; // 채팅방 이름
    private int roomUserCnt; // 같은 채팅방에 있는 유저 수
    private int chatUserCnt; // 전체 활동 중인 유저 수
    private Set<String> participants; // 현재 채팅방의 유저 목록(세션ID)
}
