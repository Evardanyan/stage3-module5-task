package com.mjc.school.controller.exception;

import java.time.OffsetDateTime;

public class CustomErrorResponse {
    private int status;
    private String message;

    private String exceptionName;
    private OffsetDateTime timestamp;

    public CustomErrorResponse() {
    }

    public CustomErrorResponse(String message, String exceptionName, OffsetDateTime timeStamp) {
        this.message = message;
        this.exceptionName = exceptionName;
        this.timestamp = timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
