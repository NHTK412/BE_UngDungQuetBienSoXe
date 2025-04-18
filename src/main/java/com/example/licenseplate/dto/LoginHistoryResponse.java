// NEW FILE
// Created on 2023-10-05
// LoginHistoryResponse.java

package com.example.licenseplate.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LoginHistoryResponse {
    private Integer id;
    private String accountId;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private String ipAddress;
    private String deviceInfo;
    private String loginStatus;
}