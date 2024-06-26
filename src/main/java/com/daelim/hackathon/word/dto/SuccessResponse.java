package com.daelim.hackathon.word.dto;

public class SuccessResponse {
    private boolean success;
    private String message;

    public SuccessResponse() {}

    public SuccessResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
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
}