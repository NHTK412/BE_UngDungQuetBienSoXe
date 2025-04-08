package com.example.licenseplate.service;

import com.example.licenseplate.model.CarViolation;
import com.example.licenseplate.model.MotorcycleViolation;
import com.example.licenseplate.repository.CarViolationRepository;
import com.example.licenseplate.repository.MotorcycleViolationRepository;
import org.springframework.stereotype.Service;

@Service
public class ViolationService {

    private final CarViolationRepository carViolationRepository;
    private final MotorcycleViolationRepository motorcycleViolationRepository;

    public ViolationService(CarViolationRepository carViolationRepository,
                            MotorcycleViolationRepository motorcycleViolationRepository) {
        this.carViolationRepository = carViolationRepository;
        this.motorcycleViolationRepository = motorcycleViolationRepository;
    }

    public CarViolation createCarViolation(CarViolation violation) {
        return carViolationRepository.save(violation);
    }

    public MotorcycleViolation createMotorcycleViolation(MotorcycleViolation violation) {
        return motorcycleViolationRepository.save(violation);
    }
}
