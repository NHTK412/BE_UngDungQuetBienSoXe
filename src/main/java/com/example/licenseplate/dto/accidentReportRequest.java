package com.example.licenseplate.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class accidentReportRequest {

    @NotBlank(message = "Camera ID is required")
    private String cameraId;

    @NotBlank(message = "Road name is required")
    private String roadName;

    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;

    private String accidentImageBase64;

    private String accidentType = "car_crash";
}
