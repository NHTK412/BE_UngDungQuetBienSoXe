package com.example.licenseplate.service;

import com.example.licenseplate.model.Car;
import com.example.licenseplate.model.Motorcycle;
import com.example.licenseplate.repository.CarRepository;
import com.example.licenseplate.repository.MotorcycleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final CarRepository carRepository;
    private final MotorcycleRepository motorcycleRepository;

    public VehicleService(CarRepository carRepository, MotorcycleRepository motorcycleRepository) {
        this.carRepository = carRepository;
        this.motorcycleRepository = motorcycleRepository;
    }
    // Car operations
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
    public Optional<Car> getCarByLicensePlate(String licensePlate) {
        return carRepository.findByLicensePlate(licensePlate);
    }
    public Car createCar(Car car) {
        return carRepository.save(car);
    }
    public Car updateCar(String licensePlate, Car updatedCar) {
        return carRepository.findByLicensePlate(licensePlate).map(car -> {
            car.setLicensePlate(updatedCar.getLicensePlate());
            car.setBrand(updatedCar.getBrand());
            car.setColor(updatedCar.getColor());
            car.setOwnerId(updatedCar.getOwnerId());
            return carRepository.save(car);
        }).orElseThrow(() -> new RuntimeException("Car not found"));
    }
    public void deleteCar(String licensePlate) {
        carRepository.deleteByLicensePlate(licensePlate);
    }
    // Motorcycle operations
    public List<Motorcycle> getAllMotorcycles() {
        return motorcycleRepository.findAll();
    }
    public Optional<Motorcycle> getMotorcycleByLicensePlate(String licensePlate) {
        return motorcycleRepository.findByLicensePlate(licensePlate);
    }
    public Motorcycle createMotorcycle(Motorcycle motorcycle) {
        return motorcycleRepository.save(motorcycle);
    }
    public Motorcycle updateMotorcycle(String licensePlate, Motorcycle updatedMotorcycle) {
        return motorcycleRepository.findByLicensePlate(licensePlate).map(motorcycle -> {
            motorcycle.setLicensePlate(updatedMotorcycle.getLicensePlate());
            motorcycle.setBrand(updatedMotorcycle.getBrand());
            motorcycle.setColor(updatedMotorcycle.getColor());
            motorcycle.setOwnerId(updatedMotorcycle.getOwnerId());
            return motorcycleRepository.save(motorcycle);
        }).orElseThrow(() -> new RuntimeException("Motorcycle not found"));
    }
    public void deleteMotorcycle(String licensePlate) {
        motorcycleRepository.deleteByLicensePlate(licensePlate);
    }
}
