package com.example.licenseplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.licenseplate.model.DrivingLicense;
import java.util.Optional;
import java.util.List;




public interface DrivingLicenseRepository extends JpaRepository<DrivingLicense, Integer> {
    
    List<DrivingLicense> findByLicenseNumber(String licenseNumber);
   
    List<DrivingLicense> findByPersonId(String personId);
}

