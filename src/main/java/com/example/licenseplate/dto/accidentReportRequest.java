package com.example.licenseplate.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AccidentReportRequest {

    @NotBlank(message = "Camera ID is required")
    private String cameraId;

    private String roadName;

    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;

    private String accidentUrl;

    private String accidentType = "car_crash";
}
