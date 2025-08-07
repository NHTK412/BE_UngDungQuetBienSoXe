package com.example.licenseplate.repository;

import com.example.licenseplate.model.responder;
import com.example.licenseplate.model.responder.ResponderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface responderRepository extends JpaRepository<responder, Integer> {

    List<responder> findByAccidentId(Integer accidentId);

    List<responder> findByUnitId(String unitId);

    List<responder> findByStatus(ResponderStatus status);

    Optional<responder> findByAccidentIdAndUnitId(Integer accidentId, String unitId);

    @Query("SELECT r FROM responder r WHERE r.unitId = :unitId ORDER BY r.assignedAt DESC")
    List<responder> findByUnitIdOrderByAssignedAtDesc(@Param("unitId") String unitId);

    @Query("SELECT r FROM responder r JOIN r.accident a WHERE r.unitId = :unitId AND r.status <> :cancelledStatus ORDER BY a.timestamp DESC")
    List<responder> findActiveAssignmentsByUnitId(@Param("unitId") String unitId, @Param("cancelledStatus") ResponderStatus cancelledStatus);
}