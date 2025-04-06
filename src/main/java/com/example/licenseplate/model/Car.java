package com.example.licenseplate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Car")
@Data
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CarId;

    @Column(name = "Ten_mau_xe", nullable = false, length = 100)
    private String tenXeCar;

    @Column(name = "Bien_so_xe", nullable = false, length = 100)
    private String bienSoCar;

    @Column(name = "mau_xe", nullable =  false, length = 100)
    private String mauXeCar;

    @Column(name = "mau_bien_so", nullable = false, length = 100)
    private String mauBienSoCar;

}
