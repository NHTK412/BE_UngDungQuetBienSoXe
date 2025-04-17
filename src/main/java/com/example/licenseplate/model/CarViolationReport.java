// New File
// CarViolationReport.java

package com.example.licenseplate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "car_violations_report")
@Data
@NoArgsConstructor
public class CarViolationReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "License plate is required")
    @Column(name = "license_plate", nullable = false, length = 20)
    private String licensePlate;

    @NotBlank(message = "Violator ID is required")
    @Column(name = "violator_id", nullable = false, length = 12)
    private String violatorId;

    @NotBlank(message = "Officer ID is required")
    @Column(name = "officer_id", nullable = false, length = 12)
    private String officerId;

    @NotNull(message = "Report time is required")
    @Column(name = "report_time", nullable = false)
    private LocalDateTime reportTime;

    @NotBlank(message = "Report location is required")
    @Column(name = "report_location", nullable = false, columnDefinition = "TEXT")
    private String reportLocation;

    @NotBlank(message = "Penalty type is required")
    @Column(name = "penalty_type", nullable = false, columnDefinition = "TEXT")
    private String penaltyType;

    @NotNull(message = "Resolution deadline is required")
    @Column(name = "resolution_deadline", nullable = false)
    private LocalDateTime resolutionDeadline;
    
    @Column(name = "resolution_status", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean resolutionStatus = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "license_plate", referencedColumnName = "license_plate", insertable = false, updatable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "violator_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Person violator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "officer_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Account officer;
}