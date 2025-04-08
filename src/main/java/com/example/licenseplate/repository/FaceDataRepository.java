package com.example.licenseplate.repository;

import com.example.licenseplate.model.FaceData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaceDataRepository extends JpaRepository<FaceData, Integer> {
}
