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

    @Column(name = "ten_mau_xe", nullable = false, length = 100)
    private String tenXeCar;

    @Column(name = "Bien_so_xe", nullable = false, length = 100)
    private String bienSoCar;

    @Column(name = "mau_xe", nullable =  false, length = 100)
    private String mauXeCar;

    @Column(name = "brand_car", nullable = false, length = 100)
    private String brandCar;

    @Column(name = "ten_chu_xe_car", nullable = false, length = 100)
    private String tenChuXeCar;

    @Column(name = "lien_he_chu_so_huu", nullable = false, length = 100)
    private String lienHeCar;


}
