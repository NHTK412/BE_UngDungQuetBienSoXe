package com.example.licenseplate.controller;

import com.example.licenseplate.model.Car;
import com.example.licenseplate.model.Motorcycle;
import com.example.licenseplate.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // GET endpoints – sử dụng bởi cả USER và ADMIN
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(vehicleService.getAllCars());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cars/{licensePlate}")
    public ResponseEntity<Car> getCarByLicensePlate(@PathVariable String licensePlate) {
        return vehicleService.getCarByLicensePlate(licensePlate)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/motorcycles")
    public ResponseEntity<List<Motorcycle>> getAllMotorcycles() {
        return ResponseEntity.ok(vehicleService.getAllMotorcycles());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/motorcycles/{licensePlate}")
    public ResponseEntity<Motorcycle> getMotorcycleByLicensePlate(@PathVariable String licensePlate) {
        return vehicleService.getMotorcycleByLicensePlate(licensePlate)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Các endpoints cập nhật, xóa xe chỉ dành cho ADMIN
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cars")
    public ResponseEntity<Car> createCar(@Valid @RequestBody Car car) {
        Car created = vehicleService.createCar(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/cars/{licensePlate}")
    public ResponseEntity<Car> updateCar(@PathVariable String licensePlate, @RequestBody Car car) {
        Car updated = vehicleService.updateCar(licensePlate, car);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/cars/{licensePlate}")
    public ResponseEntity<Void> deleteCar(@PathVariable String licensePlate) {
        vehicleService.deleteCar(licensePlate);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/motorcycles")
    public ResponseEntity<Motorcycle> createMotorcycle(@Valid @RequestBody Motorcycle motorcycle) {
        Motorcycle created = vehicleService.createMotorcycle(motorcycle);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/motorcycles/{licensePlate}")
    public ResponseEntity<Motorcycle> updateMotorcycle(@PathVariable String licensePlate, @RequestBody Motorcycle motorcycle) {
        Motorcycle updated = vehicleService.updateMotorcycle(licensePlate, motorcycle);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/motorcycles/{licensePlate}")
    public ResponseEntity<Void> deleteMotorcycle(@PathVariable String licensePlate) {
        vehicleService.deleteMotorcycle(licensePlate);
        return ResponseEntity.noContent().build();
    }
}
