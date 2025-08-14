package com.example.licenseplate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccidentForUnitResponse {
    private Integer accidentId;
    private String roadName;
    private LocalDateTime timestamp;
    private String accidentType;
    private String imageUrl;
    private String status;
}
