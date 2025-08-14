// New File
// CarViolationDetailRepository.java

package com.example.licenseplate.repository;

import com.example.licenseplate.model.CarViolationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarViolationDetailRepository extends JpaRepository<CarViolationDetail, Integer> {
    List<CarViolationDetail> findByViolationId(Integer violationId);
}