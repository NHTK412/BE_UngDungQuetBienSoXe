package com.example.licenseplate.controller;

import com.example.licenseplate.model.Car;
import com.example.licenseplate.model.Motorcycle;
import com.example.licenseplate.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // Xe hơi endpoints
    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(vehicleService.getAllCars());
    }

    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Integer id) {
        return vehicleService.getCarById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/cars")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        Car created = vehicleService.createCar(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/cars/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Integer id, @RequestBody Car car) {
        Car updated = vehicleService.updateCar(id, car);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Integer id) {
        vehicleService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    // Xe máy endpoints
    @GetMapping("/motorcycles")
    public ResponseEntity<List<Motorcycle>> getAllMotorcycles() {
        return ResponseEntity.ok(vehicleService.getAllMotorcycles());
    }

    @GetMapping("/motorcycles/{id}")
    public ResponseEntity<Motorcycle> getMotorcycleById(@PathVariable Integer id) {
        return vehicleService.getMotorcycleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/motorcycles")
    public ResponseEntity<Motorcycle> createMotorcycle(@RequestBody Motorcycle motorcycle) {
        Motorcycle created = vehicleService.createMotorcycle(motorcycle);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/motorcycles/{id}")
    public ResponseEntity<Motorcycle> updateMotorcycle(@PathVariable Integer id, @RequestBody Motorcycle motorcycle) {
        Motorcycle updated = vehicleService.updateMotorcycle(id, motorcycle);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/motorcycles/{id}")
    public ResponseEntity<Void> deleteMotorcycle(@PathVariable Integer id) {
        vehicleService.deleteMotorcycle(id);
        return ResponseEntity.noContent().build();
    }
}
