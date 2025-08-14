// NEW FILE
// Created on 2023-10-05
// LoginHistoryRequest.java


package com.example.licenseplate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginHistoryRequest {
    @NotBlank(message = "Account ID is required")
    private String accountId;
    
    private String ipAddress;
    
    private String deviceInfo;
    
    private String loginStatus;
}