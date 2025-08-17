package com.example.licenseplate.repository;

import com.example.licenseplate.model.Responder;
import com.example.licenseplate.model.Responder.ResponderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ResponderRepository extends JpaRepository<Responder, Integer> {

    List<Responder> findByAccidentId(Integer accidentId);

    List<Responder> findByUnitId(String unitId);

    List<Responder> findByStatus(ResponderStatus status);

    Optional<Responder> findByAccidentIdAndUnitId(Integer accidentId, String unitId);

    @Query("SELECT r FROM Responder r WHERE r.unitId = :unitId ORDER BY r.assignedAt DESC")
    List<Responder> findByUnitIdOrderByAssignedAtDesc(@Param("unitId") String unitId);

    @Query("SELECT r FROM Responder r JOIN r.accident a WHERE r.unitId = :unitId AND r.status <> :cancelledStatus ORDER BY a.timestamp DESC")
    List<Responder> findActiveAssignmentsByUnitId(@Param("unitId") String unitId, @Param("cancelledStatus") ResponderStatus cancelledStatus);

}
