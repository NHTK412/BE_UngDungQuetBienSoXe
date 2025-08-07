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
public class accident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "camera_id", nullable = false, length = 50)
    private String cameraId;

    @Column(name = "road_name", nullable = false)
    private String roadName;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "accident_image_base64", columnDefinition = "LONGTEXT")
    private String accidentImageBase64;

    @Column(name = "accident_image_url", length = 500)
    private String accidentImageUrl;

    @Column(name = "accident_type", nullable = false, length = 100)
    private String accidentType = "car_crash";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "accident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<responder> responders;

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