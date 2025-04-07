
package com.example.licenseplate.service;

import com.example.licenseplate.model.Car;
import com.example.licenseplate.model.Motorcycle;
import com.example.licenseplate.repository.CarRepository;
import com.example.licenseplate.repository.MotorcycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private MotorcycleRepository motorcycleRepository;

    // -------------------- Các thao tác CRUD cho xe hơi --------------------

    // Tạo mới một xe hơi
    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    // Lấy danh sách tất cả xe hơi
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // Lấy thông tin xe hơi theo ID
    public Car getCarById(Long id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            return car.get();
        } else {
            throw new RuntimeException("Không tìm thấy xe hơi với ID: " + id);
        }
    }

    // Cập nhật thông tin xe hơi
    public Car updateCar(Long id, Car carDetails) {
        Car car = getCarById(id);
        car.setLicensePlate(carDetails.getLicensePlate());
        car.setBrand(carDetails.getBrand());
        car.setModel(carDetails.getModel());
        car.setColor(carDetails.getColor());
        car.setOwner(carDetails.getOwner());
        return carRepository.save(car);
    }

    // Xóa xe hơi
    public void deleteCar(Long id) {
        Car car = getCarById(id);
        carRepository.delete(car);
    }

    // -------------------- Các thao tác CRUD cho xe máy --------------------

    // Tạo mới một xe máy
    public Motorcycle createMotorcycle(Motorcycle motorcycle) {
        return motorcycleRepository.save(motorcycle);
    }

    // Lấy danh sách tất cả xe máy
    public List<Motorcycle> getAllMotorcycles() {
        return motorcycleRepository.findAll();
    }

    // Lấy thông tin xe máy theo ID
    public Motorcycle getMotorcycleById(Long id) {
        Optional<Motorcycle> motorcycle = motorcycleRepository.findById(id);
        if (motorcycle.isPresent()) {
            return motorcycle.get();
        } else {
            throw new RuntimeException("Không tìm thấy xe máy với ID: " + id);
        }
    }

    // Cập nhật thông tin xe máy
    public Motorcycle updateMotorcycle(Long id, Motorcycle motorcycleDetails) {
        Motorcycle motorcycle = getMotorcycleById(id);
        motorcycle.setLicensePlate(motorcycleDetails.getLicensePlate());
        motorcycle.setBrand(motorcycleDetails.getBrand());
        motorcycle.setModel(motorcycleDetails.getModel());
        motorcycle.setColor(motorcycleDetails.getColor());
        motorcycle.setOwner(motorcycleDetails.getOwner());
        return motorcycleRepository.save(motorcycle);
    }

    // Xóa xe máy
    public void deleteMotorcycle(Long id) {
        Motorcycle motorcycle = getMotorcycleById(id);
        motorcycleRepository.delete(motorcycle);
    }
}