package com.example.licenseplate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class accidentReportResponse {
    private String message;
    private Integer accidentId;
    private LocalDateTime createdAt;
}