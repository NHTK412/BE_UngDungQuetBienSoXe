package com.example.licenseplate.repository;

import com.example.licenseplate.model.MotorcycleScanLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotorcycleScanLogRepository extends JpaRepository<MotorcycleScanLog, Integer> {
}
