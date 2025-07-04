// New File
// MotorcycleViolationDetailDTO.java

package com.example.licenseplate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MotorcycleViolationDetailDTO {
    private Integer id;
    
    private Integer violationId;
    
    @NotBlank(message = "Violation type is required")
    private String violationType;
    
    @NotNull(message = "Fine amount is required")
    @Positive(message = "Fine amount must be positive")
    private BigDecimal fineAmount;
}