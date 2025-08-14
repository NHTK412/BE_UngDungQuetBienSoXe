package com.example.licenseplate.controller;

import com.example.licenseplate.dto.*;
import com.example.licenseplate.service.AccidentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/accidents")
@Slf4j
public class AccidentController {

    @Autowired
    private AccidentService accidentService;

    @PostMapping
    public ResponseEntity<?> reportAccident(
            @RequestParam MultipartFile file,
            @RequestParam Integer cameraId,
            @RequestParam String accidentType) {
        log.error(accidentType);
        AccidentReportResponse response = accidentService.reportAccident(file, cameraId, accidentType);

        return ResponseEntity.ok(response);

    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR')")
    public ResponseEntity<List<AccidentResponse>> getAllAccidents() {

        List<AccidentResponse> accidents = accidentService.getAllAccidents();

        return ResponseEntity.ok(accidents);

    }

    @GetMapping("/unit/{unitId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AccidentForUnitResponse>> getAccidentsByUnitId(
            @PathVariable String unitId) {

        List<AccidentForUnitResponse> accidents = accidentService.getAccidentsByUnitId(unitId);

        return ResponseEntity.ok(accidents);

    }

    @PutMapping("/responder")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> updateResponderStatus(

            @Valid @RequestBody UpdateresponderStatusRequest request) {
        try {

            accidentService.updateResponderStatus(request);

            String message = String.format("Responder status updated to '%s' for unit %s in accident %d.",
                    request.getStatus(), request.getUnitId(), request.getAccidentId());
            return ResponseEntity.ok(ApiResponse.success(message));

        } catch (Exception e) {
            log.error("Error updating responder status for unit: {} in accident: {}", request.getUnitId(),
                    request.getAccidentId(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update status: " + e.getMessage()));
        }
    }
}
