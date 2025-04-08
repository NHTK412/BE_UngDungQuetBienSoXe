package com.example.licenseplate.repository;

import com.example.licenseplate.model.CarViolation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarViolationRepository extends JpaRepository<CarViolation, Integer> {
}
