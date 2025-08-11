package com.example.licenseplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.licenseplate.dto.CameraDTO;
import com.example.licenseplate.model.Camera;



public interface CameraRepository extends JpaRepository<Camera, Integer>{

    Camera save(CameraDTO camera);


}
