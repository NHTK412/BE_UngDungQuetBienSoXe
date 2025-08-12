package com.example.licenseplate.controller;

import com.example.licenseplate.dto.CameraDTO;
import com.example.licenseplate.model.Camera;
import com.example.licenseplate.repository.CameraRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/camera")
public class CameraController {

    private final CameraRepository cameraRepository;

    public CameraController(CameraRepository cameraRepository) {
        this.cameraRepository = cameraRepository;
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

}
