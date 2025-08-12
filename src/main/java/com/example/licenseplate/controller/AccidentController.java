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

import java.util.List;

@RestController
@RequestMapping("/api/accidents")
@Slf4j
public class AccidentController {

    @Autowired
    private AccidentService accidentService;

    /**
     * 2. Ghi Nhận Tai Nạn (Python → Java)
     * POST http://localhost:8087/quet/api/accidents
     * Xác thực: Bearer Token đơn giản
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AccidentReportResponse> reportAccident(
            @Valid @RequestBody AccidentReportRequest request) {
        try {
            log.info("Received accident report from camera: {}", request.getCameraId());
            AccidentReportResponse response = accidentService.reportAccident(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing accident report", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AccidentReportResponse(
                            "Failed to report accident: " + e.getMessage(),
                            null,
                            null));
        }
    }

    /**
     * 3. Danh Sách Tai Nạn (Web Admin)
     * GET http://localhost:8087/quet/api/accidents
     * Xác thực: Bearer Token (JWT)
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR')")
    public ResponseEntity<List<AccidentResponse>> getAllAccidents() {
        // try {
        List<AccidentResponse> accidents = accidentService.getAllAccidents();
        // __________________
        // return ResponseEntity.ok(ApiResponse.success(accidents));

        return ResponseEntity.ok(accidents);

        // } catch (Exception e) {
        // log.error("Error getting all accidents", e);
        // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        // .body(ApiResponse.error("Failed to retrieve accidents: " + e.getMessage()));

        // }
    }

    /**
     * 4. Danh Sách Tai Nạn Theo Cán Bộ (APP)
     * GET http://localhost:8087/quet/api/accidents/unit/{unit_id}
     * Xác thực: JWT
     */
    @GetMapping("/unit/{unitId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AccidentForUnitResponse>> getAccidentsByUnitId(
            @PathVariable String unitId) {
        // try {
        // log.info("Getting accidents for unit: {}", unitId);
        // List<accidentForUnitResponse> accidents =
        // accidentService.getAccidentsByUnitId(unitId);
        // return ResponseEntity.ok(ApiResponse.success(accidents));
        // } catch (Exception e) {
        // log.error("Error getting accidents for unit: {}", unitId, e);
        // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        // .body(ApiResponse.error("Failed to retrieve accidents: " + e.getMessage()));
        // }

        // try {
        List<AccidentForUnitResponse> accidents = accidentService.getAccidentsByUnitId(unitId);
        // __________________
        // return ResponseEntity.ok(ApiResponse.success(accidents));

        return ResponseEntity.ok(accidents);

        // } catch (Exception e) {
        // log.error("Error getting all accidents", e);
        // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        // .body(ApiResponse.error("Failed to retrieve accidents: " + e.getMessage()));

        // }
    }

    /**
     * 5. Cập Nhật Trạng Thái Xử Lý Tai Nạn
     * PUT
     * http://localhost:8087/quet/api/accidents/{accident_id}/responder/{unit_id}
     * Xác thực: JWT
     *
     * Trạng thái có thể có:
     * - wait: chờ xử lý (mặc định)
     * - en_route: đang đến
     * - arrived: đã đến
     * - cancelled: đã hủy
     */
    // @PutMapping("/{accidentId}/responder/{unitId}")
    // @PreAuthorize("isAuthenticated()")
    // public ResponseEntity<ApiResponse<String>> updateResponderStatus(
    // @PathVariable Integer accidentId,
    // @PathVariable String unitId,
    // @Valid @RequestBody UpdateresponderStatusRequest request) {
    // try {
    // log.info("Updating responder status for unit: {} in accident: {} to status:
    // {}",
    // unitId, accidentId, request.getStatus());

    // // Set accident ID từ path parameter vào request
    // request.setAccidentId(accidentId);

    // accidentService.updateResponderStatus(unitId, request);

    // String message = String.format("Responder status updated to '%s' for unit %s
    // in accident %d.",
    // request.getStatus(), unitId, accidentId);
    // return ResponseEntity.ok(ApiResponse.success(message));

    // } catch (Exception e) {
    // log.error("Error updating responder status for unit: {} in accident: {}",
    // unitId, accidentId, e);
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    // .body(ApiResponse.error("Failed to update status: " + e.getMessage()));
    // }
    // }

    @PutMapping("/responder")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> updateResponderStatus(
            // @PathVariable Integer accidentId,
            // @PathVariable String unitId,
            @Valid @RequestBody UpdateresponderStatusRequest request) {
        try {
            // log.info("Updating responder status for unit: {} in accident: {} to status: {}",
                    // unitId, accidentId, request.getStatus());

            // Set accident ID từ path parameter vào request
            // request.setAccidentId(accidentId);

            accidentService.updateResponderStatus(request);

            String message = String.format("Responder status updated to '%s' for unit %s in accident %d.",
                    request.getStatus(), request.getUnitId(), request.getAccidentId());
            return ResponseEntity.ok(ApiResponse.success(message));

        } catch (Exception e) {
            log.error("Error updating responder status for unit {} accident {}",
                    request.getUnitId(), request.getAccidentId(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update status: " + e.getMessage()));
        }
    }
}