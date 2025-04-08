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
    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Integer id) {
        return vehicleService.getCarById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/motorcycles")
    public ResponseEntity<List<Motorcycle>> getAllMotorcycles() {
        return ResponseEntity.ok(vehicleService.getAllMotorcycles());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/motorcycles/{id}")
    public ResponseEntity<Motorcycle> getMotorcycleById(@PathVariable Integer id) {
        return vehicleService.getMotorcycleById(id)
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
    @PutMapping("/cars/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Integer id, @RequestBody Car car) {
        Car updated = vehicleService.updateCar(id, car);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Integer id) {
        vehicleService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/motorcycles")
    public ResponseEntity<Motorcycle> createMotorcycle(@Valid @RequestBody Motorcycle motorcycle) {
        Motorcycle created = vehicleService.createMotorcycle(motorcycle);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/motorcycles/{id}")
    public ResponseEntity<Motorcycle> updateMotorcycle(@PathVariable Integer id, @RequestBody Motorcycle motorcycle) {
        Motorcycle updated = vehicleService.updateMotorcycle(id, motorcycle);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/motorcycles/{id}")
    public ResponseEntity<Void> deleteMotorcycle(@PathVariable Integer id) {
        vehicleService.deleteMotorcycle(id);
        return ResponseEntity.noContent().build();
    }
}
