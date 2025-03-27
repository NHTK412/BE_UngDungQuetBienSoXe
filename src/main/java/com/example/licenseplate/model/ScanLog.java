package com.example.licenseplate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "scan_log")
@Data
@NoArgsConstructor
public class ScanLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    /* khi nao co Vehicle */
//    private Vehicle vehicle;

    // Lưu id camera (có thể thay bằng entity nếu cần)
    @Column
    private Long cameraId;

    @Column(nullable = false)
    private LocalDateTime scanTimestamp = LocalDateTime.now();

    @Column
    private LocalDateTime operatorScanTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scanned_by")
    private User scannedBy;

    @Column(length = 255)
    private String imagePath;

    @Column(precision = 5, scale = 2)
    private Double confidence;
}
