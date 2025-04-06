package com.example.licenseplate.service;

import com.example.licenseplate.model.ScanLog;
//import com.example.licenseplate.model.Vehicle;
//import com.example.licenseplate.model.Vehicle.VehicleCategory;
//import com.example.licenseplate.model.User;
//import com.example.licenseplate.repository.ScanLogRepository;
//import com.example.licenseplate.repository.VehicleRepository;
//import com.example.licenseplate.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ScanService {

//    private final ScanLogRepository scanLogRepository;
//    private final VehicleRepository vehicleRepository;
//    private final UserRepository userRepository;
//
//    public ScanService(ScanLogRepository scanLogRepository,
//                       VehicleRepository vehicleRepository,
//                       UserRepository userRepository) {
//        this.scanLogRepository = scanLogRepository;
//        this.vehicleRepository = vehicleRepository;
//        this.userRepository = userRepository;
//    }
//
//    public ScanLog saveScanData(MultipartFile file, String licensePlate) throws IOException {
//        // Lưu file ảnh vào thư mục uploads
//        String uploadDir = "uploads/";
//        File directory = new File(uploadDir);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//        File destination = new File(directory, fileName);
//        file.transferTo(destination);
//
//        // Tìm kiếm hoặc tạo mới thông tin xe
//        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
//                .orElseGet(() -> {
//                    Vehicle v = new Vehicle();
//                    v.setLicensePlate(licensePlate);
//                    v.setVehicleCategory(VehicleCategory.OTO);
//                    return vehicleRepository.save(v);
//                });
//
//        // Lấy thông tin người quét từ context (nếu có), giả sử ở đây là null
//        User scannedBy = null; // Có thể được cập nhật sau theo logic xác thực
//
//        ScanLog scanLog = new ScanLog();
//        scanLog.setVehicle(vehicle);
//        scanLog.setCameraId(1L); // Ví dụ: cameraId = 1
//        scanLog.setScanTimestamp(LocalDateTime.now());
//        scanLog.setOperatorScanTime(LocalDateTime.now());
//        scanLog.setScannedBy(scannedBy);
//        scanLog.setImagePath(destination.getAbsolutePath());
//        scanLog.setConfidence(99.99);
//        return scanLogRepository.save(scanLog);
//    }
}
