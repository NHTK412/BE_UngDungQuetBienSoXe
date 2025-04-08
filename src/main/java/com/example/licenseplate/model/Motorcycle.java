package com.example.licenseplate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "motorcycle")
@Data
@NoArgsConstructor
public class Motorcycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "License plate is required")
    @Column(name = "license_plate", nullable = false, unique = true, length = 20)
    private String licensePlate;

    @NotBlank(message = "Brand is required")
    @Column(name = "brand", length = 50)
    private String brand;

    @NotBlank(message = "Color is required")
    @Column(name = "color", length = 30)
    private String color;

    @NotBlank(message = "Owner ID is required")
    @Column(name = "owner_id", length = 12, nullable = false)
    private String ownerId;  // Phải tồn tại trong bảng person (CCCD)
}
