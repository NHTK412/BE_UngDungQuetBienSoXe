// New File
// MotorcycleViolationReportRepository.java

package com.example.licenseplate.repository;

import com.example.licenseplate.model.MotorcycleViolationReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MotorcycleViolationReportRepository extends JpaRepository<MotorcycleViolationReport, Integer> {
    List<MotorcycleViolationReport> findByLicensePlate(String licensePlate);
    
    List<MotorcycleViolationReport> findByViolatorId(String violatorId);
    
    List<MotorcycleViolationReport> findByOfficerId(String officerId);
    
    @Query("SELECT r FROM MotorcycleViolationReport r WHERE r.reportTime BETWEEN :startDate AND :endDate")
    List<MotorcycleViolationReport> findByReportTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT r FROM MotorcycleViolationReport r WHERE r.resolutionDeadline < :currentDate AND r.resolutionStatus = false")
    List<MotorcycleViolationReport> findOverdueViolations(LocalDateTime currentDate);
}