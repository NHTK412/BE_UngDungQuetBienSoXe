package com.example.licenseplate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vi_pham")
@Data
@NoArgsConstructor
public class ViPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long violationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    // Chỉ một trong 2 sẽ được set (nếu vi phạm liên quan đến xe hơi hoặc xe 2 bánh)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motorcycle_id")
    private Motorcycle motorcycle;

    @Column(name = "thoi_gian_vi_pham", nullable = false)
    private LocalDateTime thoiGianViPham;

    @Column(name = "loai_vi_pham", nullable = false, length = 255)
    private String loaiViPham;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "hinh_thuc_xu_phat", length = 255)
    private String hinhThucXuPhat;

    @Column(name = "so_tien_phat", precision = 10, scale = 2)
    private Double soTienPhat;
}