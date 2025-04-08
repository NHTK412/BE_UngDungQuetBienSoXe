package com.example.licenseplate.repository;

import com.example.licenseplate.model.CarScanLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarScanLogRepository extends JpaRepository<CarScanLog, Integer> {
}
