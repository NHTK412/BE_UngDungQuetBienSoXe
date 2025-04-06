package com.example.licenseplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.licenseplate.model.Car;

public interface CarRepository extends JpaRepository<Car, Long>{
}
