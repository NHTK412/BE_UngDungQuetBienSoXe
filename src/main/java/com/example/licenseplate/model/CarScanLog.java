package com.example.licenseplate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "car_scan_logs")
@Data
@NoArgsConstructor
public class CarScanLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "scan_timestamp", nullable = false)
    private LocalDateTime scanTimestamp;

    @Column(name = "license_plate", nullable = false, length = 20)
    private String licensePlate;

    //account
    @Column(name = "operator_id", nullable = false, length = 12)
    private String operatorId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;
}
