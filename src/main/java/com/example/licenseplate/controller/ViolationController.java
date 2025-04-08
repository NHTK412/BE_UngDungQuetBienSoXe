package com.example.licenseplate.controller;

import com.example.licenseplate.model.CarViolation;
import com.example.licenseplate.model.MotorcycleViolation;
import com.example.licenseplate.service.ViolationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/violations")
@PreAuthorize("isAuthenticated()")
public class ViolationController {

    private final ViolationService violationService;

    public ViolationController(ViolationService violationService) {
        this.violationService = violationService;
    }

    @PostMapping("/car")
    public ResponseEntity<CarViolation> createCarViolation(@RequestBody CarViolation violation) {
        CarViolation saved = violationService.createCarViolation(violation);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/motorcycle")
    public ResponseEntity<MotorcycleViolation> createMotorcycleViolation(@RequestBody MotorcycleViolation violation) {
        MotorcycleViolation saved = violationService.createMotorcycleViolation(violation);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
