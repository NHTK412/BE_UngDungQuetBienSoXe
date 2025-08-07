package com.example.licenseplate.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UpdateresponderStatusRequest {

    @NotNull(message = "Accident ID is required")
    private Integer accidentId;

    @NotBlank(message = "Status is required")
    private String status; // wait, en_route, arrived, cancelled
}