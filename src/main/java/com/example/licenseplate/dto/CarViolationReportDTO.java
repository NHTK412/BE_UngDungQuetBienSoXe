// New File
// CarViolationReportDTO.java

package com.example.licenseplate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CarViolationReportDTO {
    private Integer id;
    
    @NotBlank(message = "License plate is required")
    private String licensePlate;
    
    @NotBlank(message = "Violator ID is required")
    private String violatorId;
    
    @NotBlank(message = "Officer ID is required")
    private String officerId;
    
    @NotNull(message = "Report time is required")
    private LocalDateTime reportTime;
    
    @NotBlank(message = "Report location is required")
    private String reportLocation;
    
    @NotBlank(message = "Penalty type is required")
    private String penaltyType;
    
    @NotNull(message = "Resolution deadline is required")
    private LocalDateTime resolutionDeadline;
    
    private Boolean resolutionStatus = false;
    
    private String violatorName;
    private String officerName;
    private String carBrand;
    private String carColor;
    
    // Chi tiết vi phạm
    private List<CarViolationDetailDTO> violationDetails;
}