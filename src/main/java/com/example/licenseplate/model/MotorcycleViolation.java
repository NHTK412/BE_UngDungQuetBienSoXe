package com.example.licenseplate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "motorcycle_violations")
@Data
@NoArgsConstructor
public class MotorcycleViolation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "violation_time", nullable = false)
    private LocalDateTime violationTime;

    @Column(name = "violation_type", nullable = false, length = 255)
    private String violationType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "penalty_type", length = 255)
    private String penaltyType;

    // Sử dụng BigDecimal để lưu số tiền phạt
    @Column(name = "fine_amount", precision = 10, scale = 2)
    private BigDecimal fineAmount;

    @Column(name = "violator_id", length = 12, nullable = false)
    private String violatorId;  // FK tới Person(id)

    @Column(name = "officer_id", length = 12, nullable = false)
    private String officerId;   // FK tới Account(id)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motorcycle_id", nullable = false)
    private Motorcycle motorcycle;
}
