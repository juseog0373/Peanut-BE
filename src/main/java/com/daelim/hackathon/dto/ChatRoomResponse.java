package com.daelim.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class ChatRoomResponse {
    private String roomId;
    private String name;
    private int roomUserCnt;
    private Set<String> chatUserCnt;
}
