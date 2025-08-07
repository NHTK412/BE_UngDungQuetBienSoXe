package com.example.licenseplate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "responder",
        uniqueConstraints = @UniqueConstraint(columnNames = {"accident_id", "unit_id"}))
@Data
@NoArgsConstructor
public class responder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accident_id", nullable = false)
    private accident accident;

    @Column(name = "unit_id", nullable = false, length = 50)
    private String unitId;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_type", nullable = false)
    private UnitType unitType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ResponderStatus status = ResponderStatus.WAIT;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Enums
    public enum UnitType {
        AMBULANCE("ambulance"),
        FIRE_DEPARTMENT("fire_department"),
        TRAFFIC_POLICE("traffic_police");

        private final String value;

        UnitType(String value) { this.value = value; }

        public String getValue() { return value; }
    }

    public enum ResponderStatus {
        WAIT("wait"),
        EN_ROUTE("en_route"),
        ARRIVED("arrived"),
        CANCELLED("cancelled");

        private final String value;

        ResponderStatus(String value) { this.value = value; }

        public String getValue() { return value; }
    }

    @PrePersist
    protected void onCreate() {
        assignedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}