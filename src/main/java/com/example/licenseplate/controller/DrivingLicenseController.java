package com.example.licenseplate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.example.licenseplate.model.DrivingLicense;
import com.example.licenseplate.repository.DrivingLicenseRepository;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

@RestController
@RequestMapping("/api/driving-license")
@PreAuthorize("isAuthenticated()")
public class DrivingLicenseController {
    
    // Inject repository
    @Autowired
    private DrivingLicenseRepository drivingLicenseRepository;
    
    @GetMapping("/license/{licenseNumber}")
    public ResponseEntity<DrivingLicense> getDrivingLicenseByLicenseNumber(@PathVariable String licenseNumber) {
        // Tìm kiếm giấy phép lái xe theo licenseNumber
        Optional<DrivingLicense> drivingLicenseOptional = drivingLicenseRepository.findByLicenseNumber(licenseNumber);
        
        // Kiểm tra xem có tìm thấy giấy phép lái xe không
        if (drivingLicenseOptional.isPresent()) {
            return ResponseEntity.ok(drivingLicenseOptional.get());
        } else {
            return ResponseEntity.notFound().build(); // Trả về mã 404 nếu không tìm thấy
        }
    }
    
    @GetMapping("/person/{personId}")
    public ResponseEntity<DrivingLicense> getDrivingLicenseByPersonId(@PathVariable String personId) {
        Optional<DrivingLicense> drivingLicenseOptional = drivingLicenseRepository.findByPersonId(personId);
        
        if (drivingLicenseOptional.isPresent()) {
            return ResponseEntity.ok(drivingLicenseOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}