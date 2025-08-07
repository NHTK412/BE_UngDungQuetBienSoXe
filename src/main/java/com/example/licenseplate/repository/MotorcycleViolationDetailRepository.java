// New File
// MotorcycleViolationDetailRepository.java

package com.example.licenseplate.repository;

import com.example.licenseplate.model.MotorcycleViolationDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotorcycleViolationDetailRepository extends JpaRepository<MotorcycleViolationDetail, Integer> {
    List<MotorcycleViolationDetail> findByViolationId(Integer violationId);
}