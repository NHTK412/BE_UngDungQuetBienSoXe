package com.example.licenseplate.repository;

import com.example.licenseplate.model.accident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface accidentRepository extends JpaRepository<accident, Integer> {

    List<accident> findByRoadNameContaining(String roadName);

    List<accident> findByCameraId(String cameraId);

    List<accident> findByAccidentType(String accidentType);

    List<accident> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT a FROM accident a ORDER BY a.timestamp DESC")
    List<accident> findAllOrderByTimestampDesc();
}