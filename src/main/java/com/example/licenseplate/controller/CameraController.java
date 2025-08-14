package com.example.licenseplate.controller;

import com.example.licenseplate.dto.CameraDTO;
import com.example.licenseplate.model.Accident;
import com.example.licenseplate.model.Camera;
import com.example.licenseplate.model.Responder;
import com.example.licenseplate.repository.AccidentRepository;
import com.example.licenseplate.repository.CameraRepository;
import com.example.licenseplate.repository.ResponderRepository;
import com.example.licenseplate.service.GoongDistanceMatrixService;

import jakarta.persistence.criteria.CriteriaBuilder.In;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/camera")
public class CameraController {

    private final CameraRepository cameraRepository;

    @Autowired
    private GoongDistanceMatrixService goongDistanceMatrixService;

    private final ResponderRepository responderRepository;

    private final AccidentRepository accidentRepository;

    public CameraController(CameraRepository cameraRepository, ResponderRepository responderRepository, AccidentRepository accidentRepository) {
        this.cameraRepository = cameraRepository;
        this.responderRepository = responderRepository;
        this.accidentRepository = accidentRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllCameras() {
        try {
            List<Camera> cameras = cameraRepository.findAll();
            return ResponseEntity.ok(cameras);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(

                    "message", "Error retrieving cameras",
                    "error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> postCreateCamera(@RequestBody Camera camera) {
        try {
            // Validate latitude & longitude
            if (camera.getLatitude() == null || camera.getLongtitude() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(

                        "message", "Latitude and Longitude are required"));
            }

            Camera newCamera = cameraRepository.save(camera);
            return ResponseEntity.status(HttpStatus.CREATED).body(new CameraDTO(newCamera));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(

                    "message", "Error creating camera",
                    "error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCameraById(@PathVariable Integer id) {
        try {
            if (!cameraRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(

                        "message", "Camera not found"));
            }
            cameraRepository.deleteById(id);

            if (!cameraRepository.existsById(id)) {
                return ResponseEntity.ok(Map.of(

                        "message", "Camera deleted successfully"));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(

                    "message", "Failed to delete camera"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(

                    "message", "Error deleting camera",
                    "error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCameraById(@PathVariable Integer id, @RequestBody Camera cameraData) {
        try {
            Optional<Camera> cameraOpt = cameraRepository.findById(id);
            if (cameraOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(

                        "message", "Camera not found"));
            }

            // Validate latitude & longitude khi update
            if (cameraData.getLatitude() == null || cameraData.getLongtitude() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(

                        "message", "Latitude and Longitude are required"));
            }

            Camera camera = cameraOpt.get();
            // Brand không bắt buộc → chỉ update nếu truyền vào
            if (cameraData.getBrand() != null) {
                camera.setBrand(cameraData.getBrand());
            }
            camera.setLatitude(cameraData.getLatitude());
            camera.setLongtitude(cameraData.getLongtitude());
            camera.setRoadName(cameraData.getRoadName());

            Camera updatedCamera = cameraRepository.save(camera);

            return ResponseEntity.ok(new CameraDTO(updatedCamera));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(

                    "message", "Error updating camera",
                    "error", e.getMessage()));
        }
    }

    @GetMapping("/{accidentId}")
    public ResponseEntity<?> getLocationCamere(
            @PathVariable Integer accidentId,
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam String unitId) {

        try {

            Optional<Accident> aOptional = accidentRepository.findById(accidentId);


            Optional<Camera> camera = cameraRepository.findById(aOptional.get().getCamera().getId());


            String origin = String.format(Locale.US, "%.6f,%.6f",
                    lat,
                    lng);
            String destination = String.format(Locale.US, "%.6f,%.6f",
                    camera.get().getLatitude().doubleValue(),
                    camera.get().getLongtitude().doubleValue());

            Double distance = goongDistanceMatrixService.getDistanceMatrix(origin, destination, "bike");

            Optional<Responder> rOptional = responderRepository.findByAccidentIdAndUnitId(accidentId, unitId);

            // LocalDateTime timestamp =

            return ResponseEntity.ok(Map.of(
                    "distance", distance,
                    "longitude", camera.get().getLongtitude().doubleValue(),
                    "latitude", camera.get().getLatitude().doubleValue(),
                    "timestamp", rOptional.get().getUpdatedAt()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(

                    "message", "Error deleting camera",
                    "error", e.getMessage()));
        }

    }
}
