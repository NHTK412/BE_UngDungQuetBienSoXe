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
import java.util.List;

@RestController
@RequestMapping("/api/driving-license")
@PreAuthorize("isAuthenticated()")
public class DrivingLicenseController {

    // Inject repository
    @Autowired
    private DrivingLicenseRepository drivingLicenseRepository;

    @GetMapping("/license/{licenseNumber}")
    public ResponseEntity<List<DrivingLicense>> getDrivingLicenseByLicenseNumber(@PathVariable String licenseNumber) {
        // Tìm kiếm giấy phép lái xe theo licenseNumber
        List<DrivingLicense> listDrivingLicense = drivingLicenseRepository.findByLicenseNumber(licenseNumber);

        // Kiểm tra xem có tìm thấy giấy phép lái xe không
        if (listDrivingLicense.size() != 0) {
            return ResponseEntity.ok(listDrivingLicense);
        } else {
            return ResponseEntity.notFound().build(); // Trả về mã 404 nếu không tìm thấy
        }
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<List<DrivingLicense>> getDrivingLicenseByPersonId(@PathVariable String personId) {
        List<DrivingLicense> listDrivingLicense = drivingLicenseRepository.findByPersonId(personId);

        if (listDrivingLicense.size() != 0 ) {
            return ResponseEntity.ok(listDrivingLicense);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}