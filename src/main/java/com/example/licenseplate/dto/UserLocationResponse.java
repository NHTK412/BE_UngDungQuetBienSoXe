package com.example.licenseplate.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLocationResponse {
    private Long id;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean online;
    private String username;
    private String email;
    private String name;
}