package com.example.licenseplate.controller;

import com.example.licenseplate.dto.CarViolationRequest;
import com.example.licenseplate.dto.MotorcycleViolationRequest;
import com.example.licenseplate.model.CarViolation;
import com.example.licenseplate.model.MotorcycleViolation;
import com.example.licenseplate.service.ViolationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/violations")
@PreAuthorize("hasAuthority('ADMIN')")
public class ViolationController {

    private final ViolationService violationService;

    public ViolationController(ViolationService violationService) {
        this.violationService = violationService;
    }

    @PostMapping("/car")
    public ResponseEntity<?> createCarViolation(@Valid @RequestBody CarViolationRequest request) {
        try {
            CarViolation violation = violationService.createCarViolation(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(violation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating car violation: " + e.getMessage());
        }
    }

    @PostMapping("/motorcycle")
    public ResponseEntity<?> createMotorcycleViolation(@Valid @RequestBody MotorcycleViolationRequest request) {
        try {
            MotorcycleViolation violation = violationService.createMotorcycleViolation(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(violation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating motorcycle violation: " + e.getMessage());
        }
    }
}
