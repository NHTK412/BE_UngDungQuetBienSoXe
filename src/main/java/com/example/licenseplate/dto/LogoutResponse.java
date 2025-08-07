// New File: LoyoutResponse.java
// Created on 2023-10-05

package com.example.licenseplate.dto;

public class LogoutResponse {
    private int status;
    private String message;

    public LogoutResponse() {}

    public LogoutResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // getter và setter
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
}
