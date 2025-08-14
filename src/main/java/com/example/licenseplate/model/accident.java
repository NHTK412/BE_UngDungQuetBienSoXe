package com.example.licenseplate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "accidents")
@Data
@NoArgsConstructor
public class Accident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Quan hệ nhiều tai nạn thuộc 1 camera
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "camera_id", nullable = false)
    private Camera camera;

    @Column(name = "road_name", nullable = false)
    private String roadName;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    // Lưu url ảnh tai nạn
    @Column(name = "accident_image_url", length = 500)
    private String accidentImageUrl;

    @Column(name = "accident_type", nullable = false, length = 100)
    private String accidentType = "car_crash";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "accident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Responder> responders;

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
