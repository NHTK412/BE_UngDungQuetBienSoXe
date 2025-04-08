package com.example.licenseplate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "account")
@Data
@NoArgsConstructor
public class Account {
    @Id
    @Column(length = 12)
    private String id;  // CCCD làm ID của tài khoản

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // Role được lưu dưới dạng ENUM trong cột (chỉ 1 vai trò)
    @Column(nullable = false, columnDefinition = "ENUM('USER','ADMIN','MODERATOR') DEFAULT 'USER'")
    private String role;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
