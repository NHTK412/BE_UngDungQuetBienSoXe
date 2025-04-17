// New File
// CarViolationController.java

package com.example.licenseplate.controller;

import com.example.licenseplate.dto.CarViolationReportDTO;
import com.example.licenseplate.service.CarViolationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car-violations")
public class CarViolationController {

    @Autowired
    private CarViolationService violationService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CarViolationReportDTO>> getAllViolations() {
        List<CarViolationReportDTO> violations = violationService.getAllViolations();
        return ResponseEntity.ok(violations);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CarViolationReportDTO> getViolationById(@PathVariable Integer id) {
        CarViolationReportDTO violation = violationService.getViolationById(id);
        return ResponseEntity.ok(violation);
    }

    @GetMapping("/license-plate/{licensePlate}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CarViolationReportDTO>> getViolationsByLicensePlate(@PathVariable String licensePlate) {
        List<CarViolationReportDTO> violations = violationService.getViolationsByLicensePlate(licensePlate);
        return ResponseEntity.ok(violations);
    }

    @GetMapping("/violator/{violatorId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CarViolationReportDTO>> getViolationsByViolatorId(@PathVariable String violatorId) {
        List<CarViolationReportDTO> violations = violationService.getViolationsByViolatorId(violatorId);
        return ResponseEntity.ok(violations);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CarViolationReportDTO> createViolation(
            @Valid @RequestBody CarViolationReportDTO violationDTO) {
        CarViolationReportDTO createdViolation = violationService.createViolation(violationDTO);
        return new ResponseEntity<>(createdViolation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CarViolationReportDTO> updateViolation(
            @PathVariable Integer id,
            @Valid @RequestBody CarViolationReportDTO violationDTO) {
        CarViolationReportDTO updatedViolation = violationService.updateViolation(id, violationDTO);
        return ResponseEntity.ok(updatedViolation);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Void> deleteViolation(@PathVariable Integer id) {
        violationService.deleteViolation(id);
        return ResponseEntity.noContent().build();
    }
}