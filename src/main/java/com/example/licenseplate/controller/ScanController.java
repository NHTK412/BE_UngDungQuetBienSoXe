package com.example.licenseplate.controller;

import com.example.licenseplate.model.ScanLog;
import com.example.licenseplate.service.ScanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/scan")
public class ScanController {
// them khi phu' add vehicle vao
//    private final ScanService scanService;
//
//    public ScanController(ScanService scanService) {
//        this.scanService = scanService;
//    }
//
//    @PostMapping
//    public ResponseEntity<?> scanLicensePlate(@RequestParam("file") MultipartFile file,
//                                              @RequestParam("licensePlate") String licensePlate) {
//        try {
//            ScanLog scanLog = scanService.saveScanData(file, licensePlate);
//            return ResponseEntity.status(HttpStatus.CREATED).body(scanLog);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error processing scan: " + e.getMessage());
//        }
//    }
}
