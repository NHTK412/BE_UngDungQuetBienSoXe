package com.example.licenseplate.repository;

import com.example.licenseplate.model.Motorcycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotorcycleRepository extends JpaRepository<Motorcycle, Long> {
    // có thể thêm các phương thức truy vấn tùy chỉnh tại đây nếu cần
}