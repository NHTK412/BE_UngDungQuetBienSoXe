package com.example.licenseplate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "motorcycle_scan_logs")
@Data
@NoArgsConstructor
public class MotorcycleScanLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "scan_timestamp", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime scanTimestamp;

    @Column(name = "license_plate", nullable = false, length = 20)
    private String licensePlate;

    //account
    @Column(name = "operator_id", length = 12, nullable = false)
    private String operatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motorcycle_id")
    private Motorcycle motorcycle;
}
