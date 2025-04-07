package com.example.licenseplate.repository;

import com.example.licenseplate.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    // có thể thêm các phương thức truy vấn tùy chỉnh tại đây nếu cần
}