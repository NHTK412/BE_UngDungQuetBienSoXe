package com.example.licenseplate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "car")
@Data
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "license_plate", nullable = false, unique = true, length = 20)
    private String licensePlate;

    @Column(name = "brand", length = 50)
    private String brand;

    @Column(name = "color", length = 30)
    private String color;

    @Column(name = "owner_id", length = 12, nullable = false)
    private String ownerId;  // FK tới Person(id)
}
