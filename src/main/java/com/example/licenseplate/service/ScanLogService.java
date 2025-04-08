package com.example.licenseplate.service;

import com.example.licenseplate.model.CarScanLog;
import com.example.licenseplate.model.MotorcycleScanLog;
import com.example.licenseplate.repository.CarScanLogRepository;
import com.example.licenseplate.repository.MotorcycleScanLogRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ScanLogService {

    private final CarScanLogRepository carScanLogRepository;
    private final MotorcycleScanLogRepository motorcycleScanLogRepository;

    public ScanLogService(CarScanLogRepository carScanLogRepository,
                          MotorcycleScanLogRepository motorcycleScanLogRepository) {
        this.carScanLogRepository = carScanLogRepository;
        this.motorcycleScanLogRepository = motorcycleScanLogRepository;
    }

    public List<CarScanLog> getAllCarScanLogs() {
        return carScanLogRepository.findAll();
    }

    public List<MotorcycleScanLog> getAllMotorcycleScanLogs() {
        return motorcycleScanLogRepository.findAll();
    }
}
