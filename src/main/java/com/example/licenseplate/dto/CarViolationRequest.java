package com.example.licenseplate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CarViolationRequest {

    @NotNull(message = "Violation time is required")
    private LocalDateTime violationTime;

    @NotBlank(message = "Violation type is required")
    private String violationType;

    private String description;

    @NotBlank(message = "Penalty type is required")
    private String penaltyType;

    @NotNull(message = "Fine amount is required")
    private BigDecimal fineAmount;

    @NotBlank(message = "Violator ID is required")
    private String violatorId;

    @NotBlank(message = "Officer ID is required")
    private String officerId;

    @NotBlank(message = "License plate is required")
    private String licensePlate;
}
