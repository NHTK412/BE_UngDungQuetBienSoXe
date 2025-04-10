package com.example.licenseplate.controller;

import com.example.licenseplate.dto.CarScanLogRequest;
import com.example.licenseplate.dto.MotorcycleScanLogRequest;
import com.example.licenseplate.service.ScanLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scan_logs")
@PreAuthorize("isAuthenticated()")
public class ScanLogController {

    private final ScanLogService scanLogService;

    public ScanLogController(ScanLogService scanLogService) {
        this.scanLogService = scanLogService;
    }

    @GetMapping("/cars")
    public ResponseEntity<?> getAllCarScanLogs() {
        return ResponseEntity.ok(scanLogService.getAllCarScanLogs());
    }

    @GetMapping("/motorcycles")
    public ResponseEntity<?> getAllMotorcycleScanLogs() {
        return ResponseEntity.ok(scanLogService.getAllMotorcycleScanLogs());
    }

    // POST endpoint để tạo mới Car Scan Log
    @PostMapping("/cars")
    public ResponseEntity<?> createCarScanLog(@Valid @RequestBody CarScanLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scanLogService.createCarScanLog(request));
    }

    // POST endpoint để tạo mới Motorcycle Scan Log
    @PostMapping("/motorcycles")
    public ResponseEntity<?> createMotorcycleScanLog(@Valid @RequestBody MotorcycleScanLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scanLogService.createMotorcycleScanLog(request));
    }
}
