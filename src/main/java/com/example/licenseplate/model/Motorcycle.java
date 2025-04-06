package com.example.licenseplate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Motorcycle")
@Data
@NoArgsConstructor
public class Motorcycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MotorcycleId;

    @Column(name = "Ten_mau_xe", nullable = false, length = 100)
    private String tenXe;

    @Column(name = "Bien_so_xe", nullable = false, length = 100)
    private String bienSo;

    @Column(name = "mau_xe", nullable =  false, length = 100)
    private String mauXe;

    @Column(name = "brand_motor", nullable = false, length = 100)
    private String brandMotor;       

    @Column(name = "ten_chu_xe_motor", nullable = false, length = 100)
    private String tenChuXeMotor;

    @Column(name = "lien_he_chu_so_huu", nullable = false, length = 100)
    private String lienHeMotor;
}
