package com.example.licenseplate.repository;

import com.example.licenseplate.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Integer> {
    Optional<Car> findByLicensePlate(String licensePlate);
}
