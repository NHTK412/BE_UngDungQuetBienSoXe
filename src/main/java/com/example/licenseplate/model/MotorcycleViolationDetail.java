// New File
// MotorcycleViolationDetail.java

package com.example.licenseplate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "motorcycle_violation_details")
@Data
@NoArgsConstructor
public class MotorcycleViolationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Violation ID is required")
    @Column(name = "violation_id", nullable = false)
    private Integer violationId;

    @NotBlank(message = "Violation type is required")
    @Column(name = "violation_type", nullable = false, length = 100)
    private String violationType;

    @NotNull(message = "Fine amount is required")
    @Positive(message = "Fine amount must be positive")
    @Column(name = "fine_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal fineAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "violation_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CarViolationReport violation;
}