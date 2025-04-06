package com.example.licenseplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.licenseplate.model.Motorcycle;

public interface MotorcycleRepository extends JpaRepository<Motorcycle, Long>{
}
