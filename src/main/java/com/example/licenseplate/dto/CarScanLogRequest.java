package com.example.licenseplate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CarScanLogRequest {
    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @NotBlank(message = "Operator ID is required")
    private String operatorId;
}
