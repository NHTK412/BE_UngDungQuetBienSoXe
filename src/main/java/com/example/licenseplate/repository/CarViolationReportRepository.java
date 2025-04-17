// New File
// CarViolationReportRepository.java

package com.example.licenseplate.repository;

import com.example.licenseplate.model.CarViolationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CarViolationReportRepository extends JpaRepository<CarViolationReport, Integer> {
    List<CarViolationReport> findByLicensePlate(String licensePlate);
    
    List<CarViolationReport> findByViolatorId(String violatorId);
    
    List<CarViolationReport> findByOfficerId(String officerId);
    
    @Query("SELECT r FROM CarViolationReport r WHERE r.reportTime BETWEEN :startDate AND :endDate")
    List<CarViolationReport> findByReportTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT r FROM CarViolationReport r WHERE r.resolutionDeadline < :currentDate AND r.resolutionStatus = false")
    List<CarViolationReport> findOverdueViolations(LocalDateTime currentDate);
}