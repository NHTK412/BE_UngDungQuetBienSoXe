package com.example.licenseplate.controller;

import com.example.licenseplate.service.ScanLogService;
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
}
