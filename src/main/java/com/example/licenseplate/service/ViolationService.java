package com.example.licenseplate.service;

import com.example.licenseplate.dto.CarViolationRequest;
import com.example.licenseplate.dto.MotorcycleViolationRequest;
import com.example.licenseplate.model.Car;
import com.example.licenseplate.model.CarViolation;
import com.example.licenseplate.model.Motorcycle;
import com.example.licenseplate.model.MotorcycleViolation;
import com.example.licenseplate.repository.CarRepository;
import com.example.licenseplate.repository.CarViolationRepository;
import com.example.licenseplate.repository.MotorcycleRepository;
import com.example.licenseplate.repository.MotorcycleViolationRepository;
import org.springframework.stereotype.Service;

@Service
public class ViolationService {

    private final CarViolationRepository carViolationRepository;
    private final MotorcycleViolationRepository motorcycleViolationRepository;
    private final CarRepository carRepository;
    private final MotorcycleRepository motorcycleRepository;

    public ViolationService(CarViolationRepository carViolationRepository,
                            MotorcycleViolationRepository motorcycleViolationRepository,
                            CarRepository carRepository,
                            MotorcycleRepository motorcycleRepository) {
        this.carViolationRepository = carViolationRepository;
        this.motorcycleViolationRepository = motorcycleViolationRepository;
        this.carRepository = carRepository;
        this.motorcycleRepository = motorcycleRepository;
    }

    public CarViolation createCarViolation(CarViolationRequest request) {
        CarViolation violation = new CarViolation();
        violation.setViolationTime(request.getViolationTime());
        violation.setViolationType(request.getViolationType());
        violation.setDescription(request.getDescription());
        violation.setPenaltyType(request.getPenaltyType());
        violation.setFineAmount(request.getFineAmount());
        violation.setViolatorId(request.getViolatorId());
        violation.setOfficerId(request.getOfficerId());

        // Tìm đối tượng Car bằng biển số xe
        Car car = carRepository.findByLicensePlate(request.getLicensePlate())
                .orElseThrow(() -> new RuntimeException("Car not found with license plate " + request.getLicensePlate()));
        violation.setCar(car);

        return carViolationRepository.save(violation);
    }

    public MotorcycleViolation createMotorcycleViolation(MotorcycleViolationRequest request) {
        MotorcycleViolation violation = new MotorcycleViolation();
        violation.setViolationTime(request.getViolationTime());
        violation.setViolationType(request.getViolationType());
        violation.setDescription(request.getDescription());
        violation.setPenaltyType(request.getPenaltyType());
        violation.setFineAmount(request.getFineAmount());
        violation.setViolatorId(request.getViolatorId());
        violation.setOfficerId(request.getOfficerId());

        // Tìm đối tượng Motorcycle bằng ID (bạn có thể thay đổi để tìm theo licensePlate nếu mong muốn)
        Motorcycle motorcycle = motorcycleRepository.findByLicensePlate(request.getLicensePlate())
                .orElseThrow(() -> new RuntimeException("Motorcycle not flicense plate " + request.getLicensePlate()));
        violation.setMotorcycle(motorcycle);

        return motorcycleViolationRepository.save(violation);
    }
}
