package com.example.licenseplate.repository;

import com.example.licenseplate.model.Accident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface AccidentRepository extends JpaRepository<Accident, Integer> {

    List<Accident> findByRoadNameContaining(String roadName);

    List<Accident> findByCameraId(Integer cameraId);

    List<Accident> findByAccidentType(String accidentType);

    List<Accident> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT a FROM Accident a ORDER BY a.timestamp DESC")
    List<Accident> findAllOrderByTimestampDesc();
}