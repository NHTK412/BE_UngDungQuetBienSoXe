package com.example.licenseplate.repository;

import com.example.licenseplate.model.MotorcycleViolation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotorcycleViolationRepository extends JpaRepository<MotorcycleViolation, Integer> {
}
