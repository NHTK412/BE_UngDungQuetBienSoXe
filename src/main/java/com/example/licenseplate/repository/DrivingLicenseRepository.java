package com.example.licenseplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.licenseplate.model.DrivingLicense;
import java.util.Optional;



public interface DrivingLicenseRepository extends JpaRepository<DrivingLicense, Integer> {
    
    Optional<DrivingLicense> findByLicenseNumber(String licenseNumber);
   
    Optional<DrivingLicense> findByPersonId(String personId);
}

