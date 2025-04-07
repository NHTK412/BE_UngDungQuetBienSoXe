package com.example.licenseplate.controller;

import com.example.licenseplate.model.Car;
import com.example.licenseplate.model.Motorcycle;
import com.example.licenseplate.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    // -------------------- Các API cho xe hơi --------------------

    // Tạo mới một xe hơi
    @PostMapping("/cars")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        Car createdCar = vehicleService.createCar(car);
        return ResponseEntity.ok(createdCar);
    }

    // Lấy danh sách tất cả xe hơi
    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = vehicleService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    // Lấy thông tin xe hơi theo ID
    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Car car = vehicleService.getCarById(id);
        return ResponseEntity.ok(car);
    }

    // Cập nhật thông tin xe hơi
    @PutMapping("/cars/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car carDetails) {
        Car updatedCar = vehicleService.updateCar(id, carDetails);
        return ResponseEntity.ok(updatedCar);
    }

    // Xóa xe hơi
    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        vehicleService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------- Các API cho xe máy --------------------

    // Tạo mới một xe máy
    @PostMapping("/motorcycles")
    public ResponseEntity<Motorcycle> createMotorcycle(@RequestBody Motorcycle motorcycle) {
        Motorcycle createdMotorcycle = vehicleService.createMotorcycle(motorcycle);
        return ResponseEntity.ok(createdMotorcycle);
    }

    // Lấy danh sách tất cả xe máy
    @GetMapping("/motorcycles")
    public ResponseEntity<List<Motorcycle>> getAllMotorcycles() {
        List<Motorcycle> motorcycles = vehicleService.getAllMotorcycles();
        return ResponseEntity.ok(motorcycles);
    }

    // Lấy thông tin xe máy theo ID
    @GetMapping("/motorcycles/{id}")
    public ResponseEntity<Motorcycle> getMotorcycleById(@PathVariable Long id) {
        Motorcycle motorcycle = vehicleService.getMotorcycleById(id);
        return ResponseEntity.ok(motorcycle);
    }

    // Cập nhật thông tin xe máy
    @PutMapping("/motorcycles/{id}")
    public ResponseEntity<Motorcycle> updateMotorcycle(@PathVariable Long id, @RequestBody Motorcycle motorcycleDetails) {
        Motorcycle updatedMotorcycle = vehicleService.updateMotorcycle(id, motorcycleDetails);
        return ResponseEntity.ok(updatedMotorcycle);
    }

    // Xóa xe máy
    @DeleteMapping("/motorcycles/{id}")
    public ResponseEntity<Void> deleteMotorcycle(@PathVariable Long id) {
        vehicleService.deleteMotorcycle(id);
        return ResponseEntity.noContent().build();
    }
}