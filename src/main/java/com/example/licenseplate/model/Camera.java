package com.example.licenseplate.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "camera", indexes = {
        @Index(name = "idx_camera_road_name", columnList = "road_name"),
        @Index(name = "idx_camera_brand", columnList = "brand")
})
@Data
@NoArgsConstructor
public class Camera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "road_name", nullable = false, length = 255)
    private String roadName;

    @Column(name = "longtitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal longtitude;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "brand", nullable = false, length = 100)
    private String brand;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
