package com.example.licenseplate.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationRequest {
    private String accountId;
    private Double longitude;
    private Double latitude;
}
