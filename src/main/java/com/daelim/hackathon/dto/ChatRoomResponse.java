package com.daelim.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomResponse {
    private String roomId;
    private String name;
    private int chatUserCnt;
}
