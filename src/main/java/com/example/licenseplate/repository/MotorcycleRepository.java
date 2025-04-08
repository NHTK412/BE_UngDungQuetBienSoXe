package com.example.licenseplate.repository;

import com.example.licenseplate.model.Motorcycle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MotorcycleRepository extends JpaRepository<Motorcycle, Integer> {
    Optional<Motorcycle> findByLicensePlate(String licensePlate);
}
