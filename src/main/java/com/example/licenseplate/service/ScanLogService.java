package com.example.licenseplate.service;

import com.example.licenseplate.dto.CarScanLogRequest;
import com.example.licenseplate.dto.MotorcycleScanLogRequest;
import com.example.licenseplate.model.Car;
import com.example.licenseplate.model.CarScanLog;
import com.example.licenseplate.model.Motorcycle;
import com.example.licenseplate.model.MotorcycleScanLog;
import com.example.licenseplate.repository.CarRepository;
import com.example.licenseplate.repository.CarScanLogRepository;
import com.example.licenseplate.repository.MotorcycleRepository;
import com.example.licenseplate.repository.MotorcycleScanLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScanLogService {

    private final CarScanLogRepository carScanLogRepository;
    private final MotorcycleScanLogRepository motorcycleScanLogRepository;
    private final CarRepository carRepository;
    private final MotorcycleRepository motorcycleRepository;

    public ScanLogService(CarScanLogRepository carScanLogRepository,
                          MotorcycleScanLogRepository motorcycleScanLogRepository,
                          CarRepository carRepository,
                          MotorcycleRepository motorcycleRepository) {
        this.carScanLogRepository = carScanLogRepository;
        this.motorcycleScanLogRepository = motorcycleScanLogRepository;
        this.carRepository = carRepository;
        this.motorcycleRepository = motorcycleRepository;
    }

    public Object getAllCarScanLogs() {
        return carScanLogRepository.findAll();
    }

    public Object getAllMotorcycleScanLogs() {
        return motorcycleScanLogRepository.findAll();
    }

    public CarScanLog createCarScanLog(CarScanLogRequest request) {
        // Tìm Car bằng licensePlate
        Car car = carRepository.findByLicensePlate(request.getLicensePlate())
                .orElseThrow(() -> new RuntimeException("Car not found with license plate " + request.getLicensePlate()));
        CarScanLog scanLog = new CarScanLog();
        scanLog.setLicensePlate(request.getLicensePlate());
        scanLog.setOperatorId(request.getOperatorId());
        // Giả sử thời gian quét được set bằng LocalDateTime.now()
        scanLog.setScanTimestamp(LocalDateTime.now());
        // Bạn có thể gán đối tượng Car nếu cần (ví dụ, scanLog.setCar(car);)
        scanLog.setCar(car);
        return carScanLogRepository.save(scanLog);
    }

    public MotorcycleScanLog createMotorcycleScanLog(MotorcycleScanLogRequest request) {
        // Tìm Motorcycle bằng licensePlate nếu bạn muốn (ở đây hoặc theo id nếu có)
        // Ví dụ: cho đơn giản, ta sử dụng repository để tìm theo licensePlate
        Motorcycle motorcycle = motorcycleRepository.findByLicensePlate(request.getLicensePlate())
                .orElseThrow(() -> new RuntimeException("Motorcycle not found with license plate " + request.getLicensePlate()));
        MotorcycleScanLog scanLog = new MotorcycleScanLog();
        scanLog.setLicensePlate(request.getLicensePlate());
        scanLog.setOperatorId(request.getOperatorId());
        scanLog.setScanTimestamp(LocalDateTime.now());
        scanLog.setMotorcycle(motorcycle);
        return motorcycleScanLogRepository.save(scanLog);
    }
}
