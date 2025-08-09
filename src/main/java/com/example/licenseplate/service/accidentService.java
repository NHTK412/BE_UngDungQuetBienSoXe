package com.example.licenseplate.service;

import com.example.licenseplate.dto.*;
import com.example.licenseplate.model.Accident;
import com.example.licenseplate.model.responder;
import com.example.licenseplate.model.responder.ResponderStatus;
import com.example.licenseplate.model.responder.UnitType;
import com.example.licenseplate.repository.AccidentRepository;
import com.example.licenseplate.repository.responderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccidentService {

    @Autowired
    private AccidentRepository accidentRepository;

    @Autowired
    private responderRepository responderRepository;

    /**
     * Ghi nhận tai nạn từ Python system
     */
    @Transactional
    public AccidentReportResponse reportAccident(AccidentReportRequest request) {
        try {
            Accident accident = new Accident();
            accident.setCameraId(request.getCameraId());
            accident.setRoadName(request.getRoadName());
            accident.setTimestamp(request.getTimestamp());
            accident.setAccidentImageBase64(request.getAccidentImageBase64());
            accident.setAccidentType(request.getAccidentType() != null ? request.getAccidentType() : "car_crash");

            // Tạo URL cho image (có thể dựa vào accident ID sau khi save)
            Accident savedAccident = accidentRepository.save(accident);

            // Tạo URL cho image
            String imageUrl = generateImageUrl(savedAccident.getId());
            savedAccident.setAccidentImageUrl(imageUrl);
            accidentRepository.save(savedAccident);

            // Tự động assign responder (có thể cấu hình logic này)
            assignDefaultResponders(savedAccident);

            log.info("Accident reported successfully with ID: {}", savedAccident.getId());

            return new AccidentReportResponse(
                    "Accident reported successfully.",
                    savedAccident.getId(),
                    savedAccident.getCreatedAt());

        } catch (Exception e) {
            log.error("Error reporting accident", e);
            throw new RuntimeException("Failed to report accident: " + e.getMessage());
        }
    }

    /**
     * Lấy danh sách tất cả tai nạn cho Web Admin
     */
    public List<AccidentResponse> getAllAccidents() {
        List<Accident> accidents = accidentRepository.findAllOrderByTimestampDesc();
        return accidents.stream()
                .map(this::convertToAccidentResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách tai nạn theo unit_id cho APP
     */
    public List<AccidentForUnitResponse> getAccidentsByUnitId(String unitId) {
        // Sửa lại để truyền đúng tham số
        List<responder> responders = responderRepository.findActiveAssignmentsByUnitId(unitId,
                ResponderStatus.CANCELLED);
        return responders.stream()
                .map(this::convertToAccidentForUnitResponse)
                .collect(Collectors.toList());
    }

    /**
     * Cập nhật trạng thái responder
     */
    // @Transactional
    // public void updateResponderStatus(String unitId, UpdateresponderStatusRequest
    // request) {
    // Optional<responder> responderOpt =
    // responderRepository.findByAccidentIdAndUnitId(
    // request.getAccidentId(), unitId);

    // if (responderOpt.isEmpty()) {
    // throw new EntityNotFoundException(
    // "Responder not found for unit " + unitId + " and accident " +
    // request.getAccidentId());
    // }

    // responder responder = responderOpt.get();
    // ResponderStatus newStatus =
    // ResponderStatus.valueOf(request.getStatus().toUpperCase());
    // responder.setStatus(newStatus);

    // responderRepository.save(responder);

    // log.info("Updated responder status for unit {} to {} for accident {}",
    // unitId, newStatus, request.getAccidentId());
    // }

    @Transactional
    public void updateResponderStatus( UpdateresponderStatusRequest request) {
        Optional<responder> responderOpt = responderRepository.findByAccidentIdAndUnitId(
                request.getAccidentId(), request.getUnitId());

        if (responderOpt.isEmpty()) {
            throw new EntityNotFoundException(
                    "Responder not found for unit " + request.getUnitId() + " and accident " + request.getAccidentId());
        }

        responder responder = responderOpt.get();
        ResponderStatus newStatus = ResponderStatus.valueOf(request.getStatus().toUpperCase());
        responder.setStatus(newStatus);

        responderRepository.save(responder);

        log.info("Updated responder status for unit {} to {} for accident {}",
                request.getUnitId(), newStatus, request.getAccidentId());
    }

    /**
     * Lấy thông tin chi tiết tai nạn
     */
    public AccidentResponse getAccidentById(Integer id) {
        Accident accident = accidentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Accident not found with id: " + id));
        return convertToAccidentResponse(accident);
    }

    /**
     * Tự động assign responder mặc định
     */
    private void assignDefaultResponders(Accident accident) {
        // Logic có thể cấu hình dựa vào loại tai nạn và vị trí

        // Mặc định assign ambulance và traffic_police
        responder ambulanceResponder = new responder();
        ambulanceResponder.setAccident(accident);
        ambulanceResponder.setUnitId("unit_01"); // Có thể lấy từ config hoặc service khác
        ambulanceResponder.setUnitType(UnitType.AMBULANCE);
        ambulanceResponder.setStatus(ResponderStatus.WAIT);

        responder trafficPoliceResponder = new responder();
        trafficPoliceResponder.setAccident(accident);
        trafficPoliceResponder.setUnitId("unit_05"); // Có thể lấy từ config hoặc service khác
        trafficPoliceResponder.setUnitType(UnitType.TRAFFIC_POLICE);
        trafficPoliceResponder.setStatus(ResponderStatus.WAIT);

        responderRepository.save(ambulanceResponder);
        responderRepository.save(trafficPoliceResponder);

        log.info("Assigned default responders for accident {}", accident.getId());
    }

    /**
     * Tạo URL cho image
     */
    private String generateImageUrl(Integer accidentId) {
        // Có thể cấu hình domain từ properties
        return "https://yourdomain.com/images/accidents/" + accidentId + ".jpg";
    }

    /**
     * Convert accident entity sang accidentResponse DTO
     */
    private AccidentResponse convertToAccidentResponse(Accident accident) {
        AccidentResponse response = new AccidentResponse();
        response.setAccidentId(accident.getId());
        response.setRoadName(accident.getRoadName());
        response.setCameraId(accident.getCameraId());
        response.setTimestamp(accident.getTimestamp());
        response.setAccidentType(accident.getAccidentType());
        response.setImageUrl(accident.getAccidentImageUrl());

        // Lấy danh sách responder
        List<responder> responders = responderRepository.findByAccidentId(accident.getId());
        List<AccidentResponse.responderInfo> responderInfos = responders.stream()
                .map(r -> new AccidentResponse.responderInfo(
                        r.getUnitId(),
                        r.getUnitType().getValue(),
                        r.getStatus().getValue()))
                .collect(Collectors.toList());

        response.setResponder(responderInfos);
        return response;
    }

    /**
     * Convert responder sang accidentForUnitResponse
     */
    private AccidentForUnitResponse convertToAccidentForUnitResponse(responder responder) {
        Accident accident = responder.getAccident();
        return new AccidentForUnitResponse(
                accident.getId(),
                accident.getRoadName(),
                accident.getTimestamp(),
                accident.getAccidentType(),
                accident.getAccidentImageUrl(),
                responder.getStatus().getValue());
    }
}