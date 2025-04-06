package com.example.licenseplate.repository;

import com.example.licenseplate.model.ScanLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanLogRepository extends JpaRepository<ScanLog, Long> {
}
