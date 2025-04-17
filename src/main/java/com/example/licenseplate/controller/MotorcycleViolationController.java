// New File
// MotorcycleViolationController.java

package com.example.licenseplate.controller;

import com.example.licenseplate.dto.MotorcycleViolationReportDTO;
import com.example.licenseplate.service.MotorcycleViolationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motorcycle-violations")
public class MotorcycleViolationController {

    @Autowired
    private MotorcycleViolationService violationService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MotorcycleViolationReportDTO>> getAllViolations() {
        List<MotorcycleViolationReportDTO> violations = violationService.getAllViolations();
        return ResponseEntity.ok(violations);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MotorcycleViolationReportDTO> getViolationById(@PathVariable Integer id) {
        MotorcycleViolationReportDTO violation = violationService.getViolationById(id);
        return ResponseEntity.ok(violation);
    }

    @GetMapping("/license-plate/{licensePlate}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MotorcycleViolationReportDTO>> getViolationsByLicensePlate(@PathVariable String licensePlate) {
        List<MotorcycleViolationReportDTO> violations = violationService.getViolationsByLicensePlate(licensePlate);
        return ResponseEntity.ok(violations);
    }

    @GetMapping("/violator/{violatorId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MotorcycleViolationReportDTO>> getViolationsByViolatorId(@PathVariable String violatorId) {
        List<MotorcycleViolationReportDTO> violations = violationService.getViolationsByViolatorId(violatorId);
        return ResponseEntity.ok(violations);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MotorcycleViolationReportDTO> createViolation(
            @Valid @RequestBody MotorcycleViolationReportDTO violationDTO) {
        MotorcycleViolationReportDTO createdViolation = violationService.createViolation(violationDTO);
        return new ResponseEntity<>(createdViolation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MotorcycleViolationReportDTO> updateViolation(
            @PathVariable Integer id,
            @Valid @RequestBody MotorcycleViolationReportDTO violationDTO) {
        MotorcycleViolationReportDTO updatedViolation = violationService.updateViolation(id, violationDTO);
        return ResponseEntity.ok(updatedViolation);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Void> deleteViolation(@PathVariable Integer id) {
        violationService.deleteViolation(id);
        return ResponseEntity.noContent().build();
    }
}