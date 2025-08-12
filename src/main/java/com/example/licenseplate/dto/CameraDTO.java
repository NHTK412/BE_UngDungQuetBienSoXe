package com.example.licenseplate.dto;

import java.math.BigDecimal;

import com.example.licenseplate.model.Camera;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CameraDTO {
    private String roadName;
    private Double longtitude;
    private Double latitude;
    private String brand;

    public CameraDTO(Camera camera) {
        this.roadName = camera.getRoadName();
        this.latitude = camera.getLatitude().doubleValue();
        this.longtitude = camera.getLongtitude().doubleValue();
        this.brand = camera.getBrand();
    }
}