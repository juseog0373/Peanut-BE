package com.daelim.hackathon.word.dto;

public class GameResponse {
    private boolean success;
    private String message;
    private String lastWord;

    public GameResponse() {}

    public GameResponse(boolean success, String message, String lastWord) {
        this.success = success;
        this.message = message;
        this.lastWord = lastWord;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLastWord() {
        return lastWord;
    }

    public void setLastWord(String lastWord) {
        this.lastWord = lastWord;
    }
}
