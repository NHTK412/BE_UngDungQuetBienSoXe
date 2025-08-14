package com.example.licenseplate.service;

import com.example.licenseplate.dto.*;
import com.example.licenseplate.model.Accident;
import com.example.licenseplate.model.Responder;
import com.example.licenseplate.model.UserLocation;
import com.example.licenseplate.model.Camera;
import com.example.licenseplate.model.FcmToken;
import com.example.licenseplate.model.Responder.ResponderStatus;
import com.example.licenseplate.model.Responder.UnitType;
import com.example.licenseplate.repository.AccidentRepository;
import com.example.licenseplate.repository.CameraRepository;
import com.example.licenseplate.repository.FcmTokenRepository;
import com.example.licenseplate.repository.ResponderRepository;
import com.example.licenseplate.repository.UserLocationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccidentService {

    @Autowired
    private AccidentRepository accidentRepository;

    @Autowired
    private ResponderRepository responderRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private UserLocationRepository userLocationRepository;

    @Autowired
    private GoongDistanceMatrixService goongDistanceMatrixService;

    @Autowired
    private FcmTokenRepository fcmTokenRepository;

    @Autowired
    private FcmService fcmService;

    /**
     * Ghi nhận tai nạn từ Python system
     */

    @Transactional
    public AccidentReportResponse reportAccident(MultipartFile file, Integer cameraId, String accidentType) {
        try {
            Optional<Camera> camera = cameraRepository.findById(cameraId);
            if (!camera.isPresent()) {
                throw new EntityNotFoundException("Camera not found with id: " + cameraId);
            }

            Accident accident = new Accident();
            accident.setCamera(camera.get());
            accident.setAccidentType(accidentType);

            String filePath = imageService.saveFile(file);
            accident.setAccidentImageUrl(filePath);

            List<UserLocation> userLocationList = userLocationRepository.findAll();

            String nearestUserId = null;
            Double minDistance = Double.MAX_VALUE;

            for (UserLocation userLocation : userLocationList) {
                try {
                    // Tạo coordinates với định dạng chính xác
                    // String origin = String.format("%.6f,%.6f",
                    // userLocation.getLatitude().doubleValue(),
                    // userLocation.getLongitude().doubleValue());
                    // String destination = String.format("%.6f,%.6f",
                    // camera.get().getLatitude().doubleValue(),
                    // camera.get().getLongtitude().doubleValue());

                    String origin = String.format(Locale.US, "%.6f,%.6f",
                            userLocation.getLatitude().doubleValue(),
                            userLocation.getLongitude().doubleValue());
                    String destination = String.format(Locale.US, "%.6f,%.6f",
                            camera.get().getLatitude().doubleValue(),
                            camera.get().getLongtitude().doubleValue());

                    log.info("Calculating distance from {} to {}", origin, destination);

                    Double distance = goongDistanceMatrixService.getDistanceMatrix(origin, destination, "bike");

                    if (distance != null && distance < minDistance) {
                        minDistance = distance;
                        nearestUserId = userLocation.getAccount().getId();
                        log.info("Found closer user: {} at distance: {} km", nearestUserId, distance);
                    }
                } catch (Exception e) {
                    log.error("Error calculating distance for user {}: {}",
                            userLocation.getAccount().getId(), e.getMessage());
                    // Tiếp tục với user khác thay vì dừng lại
                    continue;
                }
            }

            // Fallback nếu không tính được distance nào
            if (nearestUserId == null && !userLocationList.isEmpty()) {
                log.warn("Could not calculate distances, using first available user");
                nearestUserId = userLocationList.get(0).getAccount().getId();
                minDistance = 0.0; // hoặc một giá trị mặc định
            }

            if (nearestUserId != null) {
                log.info("Nearest user: {} with distance: {} km", nearestUserId, minDistance);

                // Gửi notification
                try {
                    FcmToken fcmToken = fcmTokenRepository.findByAccountId(nearestUserId);
                    if (fcmToken != null) {
                        String token = fcmToken.getToken();
                        fcmService.sendNotification(token, "Thông Báo Phát Hiện Tai Nạn",
                                "Vui lòng tới vị trí tai nạn gấp");

                        // Tạo responder
                        Responder responder = new Responder();
                        responder.setAccident(accident);
                        responder.setUnitId(nearestUserId);
                        responder.setUnitType(UnitType.TRAFFIC_POLICE);
                        responder.setStatus(ResponderStatus.WAIT);

                        accident.setResponders(List.of(responder));
                    } else {
                        log.warn("No FCM token found for user: {}", nearestUserId);
                    }
                } catch (Exception e) {
                    log.error("Error sending notification to user {}: {}", nearestUserId, e.getMessage());
                }
            }

            accident.setTimestamp(LocalDateTime.now());
            accident.setRoadName(camera.get().getRoadName());

            Accident savedAccident = accidentRepository.save(accident);

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
        List<Responder> responders = responderRepository.findActiveAssignmentsByUnitId(unitId,
                ResponderStatus.CANCELLED);
        return responders.stream()
                .map(this::convertToAccidentForUnitResponse)
                .collect(Collectors.toList());
    }

    // @Transactional
    // public void updateResponderStatus(UpdateresponderStatusRequest request) {
    // Optional<Responder> responderOpt =
    // responderRepository.findByAccidentIdAndUnitId(
    // request.getAccidentId(), request.getUnitId());

    // if (responderOpt.isEmpty()) {
    // throw new EntityNotFoundException(
    // "Responder not found for unit " + request.getUnitId() + " and accident " +
    // request.getAccidentId());
    // }

    // Responder responder = responderOpt.get();
    // ResponderStatus newStatus = ResponderStatus.valueOf(null,
    // request.getStatus().toUpperCase());
    // responder.setStatus(newStatus);

    // responderRepository.save(responder);
    // log.info("Updated responder status for unit {} to {} for accident {}",
    // request.getUnitId(), newStatus, request.getAccidentId());
    // }

    @Transactional
    public void updateResponderStatus(UpdateresponderStatusRequest request) {
        Optional<Responder> responderOpt = responderRepository.findByAccidentIdAndUnitId(
                request.getAccidentId(), request.getUnitId());
        if (responderOpt.isEmpty()) {
            throw new EntityNotFoundException(
                    "Responder not found for unit " + request.getUnitId() + " and accident " + request.getAccidentId());
        }

        Responder responder = responderOpt.get();

        ResponderStatus newStatus;
        try {
            String statusValue = request.getStatus().toLowerCase();
            switch (statusValue) {
                case "wait":
                    newStatus = ResponderStatus.WAIT;
                    break;
                case "en_route":
                    newStatus = ResponderStatus.EN_ROUTE;
                    break;
                case "arrived":
                    newStatus = ResponderStatus.ARRIVED;
                    break;
                case "cancelled":
                    newStatus = ResponderStatus.CANCELLED;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid status: " + request.getStatus());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid status: " + request.getStatus(), e);
        }

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
        Responder ambulanceResponder = new Responder();
        ambulanceResponder.setAccident(accident);
        ambulanceResponder.setUnitId("unit_01"); // Có thể lấy từ config hoặc service khác
        ambulanceResponder.setUnitType(UnitType.AMBULANCE);
        ambulanceResponder.setStatus(ResponderStatus.WAIT);

        Responder trafficPoliceResponder = new Responder();
        trafficPoliceResponder.setAccident(accident);
        trafficPoliceResponder.setUnitId("unit_05"); // Có thể lấy từ config hoặc service khác
        trafficPoliceResponder.setUnitType(UnitType.TRAFFIC_POLICE);
        trafficPoliceResponder.setStatus(ResponderStatus.WAIT);
        responderRepository.save(ambulanceResponder);
        responderRepository.save(trafficPoliceResponder);
        log.info("Assigned default responders for accident {}", accident.getId());
    }

    /**
     * Convert accident entity sang accidentResponse DTO
     */
    private AccidentResponse convertToAccidentResponse(Accident accident) {
        AccidentResponse response = new AccidentResponse();
        response.setAccidentId(accident.getId());
        response.setRoadName(accident.getRoadName());
        response.setCameraId(accident.getCamera().getId());
        response.setTimestamp(accident.getTimestamp());
        response.setAccidentType(accident.getAccidentType());
        response.setImageUrl(accident.getAccidentImageUrl());

        // Lấy danh sách responder
        List<Responder> responders = responderRepository.findByAccidentId(accident.getId());
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
    private AccidentForUnitResponse convertToAccidentForUnitResponse(Responder responder) {
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
