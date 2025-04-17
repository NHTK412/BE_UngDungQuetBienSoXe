// New File
// MotorcycleViolationService.java

package com.example.licenseplate.service;

import com.example.licenseplate.dto.MotorcycleViolationDetailDTO;
import com.example.licenseplate.dto.MotorcycleViolationReportDTO;
import com.example.licenseplate.model.MotorcycleViolationDetail;
import com.example.licenseplate.model.MotorcycleViolationReport;
import com.example.licenseplate.repository.MotorcycleRepository;
import com.example.licenseplate.repository.MotorcycleViolationDetailRepository;
import com.example.licenseplate.repository.MotorcycleViolationReportRepository;
import com.example.licenseplate.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MotorcycleViolationService {

    @Autowired
    private MotorcycleViolationReportRepository violationReportRepository;

    @Autowired
    private MotorcycleViolationDetailRepository violationDetailRepository;

    @Autowired
    private MotorcycleRepository motorcycleRepository;

    @Autowired
    private PersonRepository personRepository;

    public List<MotorcycleViolationReportDTO> getAllViolations() {
        List<MotorcycleViolationReport> reports = violationReportRepository.findAll();
        return reports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MotorcycleViolationReportDTO getViolationById(Integer id) {
        MotorcycleViolationReport report = violationReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Violation report not found with id: " + id));
        return convertToDTO(report);
    }

    public List<MotorcycleViolationReportDTO> getViolationsByLicensePlate(String licensePlate) {
        List<MotorcycleViolationReport> reports = violationReportRepository.findByLicensePlate(licensePlate);
        return reports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MotorcycleViolationReportDTO> getViolationsByViolatorId(String violatorId) {
        List<MotorcycleViolationReport> reports = violationReportRepository.findByViolatorId(violatorId);
        return reports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MotorcycleViolationReportDTO createViolation(MotorcycleViolationReportDTO violationDTO) {
        // Kiểm tra xem license plate có tồn tại
        if (!motorcycleRepository.existsByLicensePlate(violationDTO.getLicensePlate())) {
            throw new EntityNotFoundException(
                    "Motorcycle not found with license plate: " + violationDTO.getLicensePlate());
        }

        // Kiểm tra người vi phạm có tồn tại
        if (!personRepository.existsById(violationDTO.getViolatorId())) {
            throw new EntityNotFoundException("Person not found with id: " + violationDTO.getViolatorId());
        }

        // Tạo mới báo cáo vi phạm
        MotorcycleViolationReport report = new MotorcycleViolationReport();
        report.setLicensePlate(violationDTO.getLicensePlate());
        report.setViolatorId(violationDTO.getViolatorId());
        report.setOfficerId(violationDTO.getOfficerId());
        report.setReportTime(violationDTO.getReportTime() != null ? violationDTO.getReportTime() : LocalDateTime.now());
        report.setReportLocation(violationDTO.getReportLocation());
        report.setPenaltyType(violationDTO.getPenaltyType());
        report.setResolutionDeadline(violationDTO.getResolutionDeadline());

        MotorcycleViolationReport savedReport = violationReportRepository.save(report);

        // Lưu chi tiết vi phạm nếu có
        if (violationDTO.getViolationDetails() != null && !violationDTO.getViolationDetails().isEmpty()) {
            for (MotorcycleViolationDetailDTO detailDTO : violationDTO.getViolationDetails()) {
                MotorcycleViolationDetail detail = new MotorcycleViolationDetail();
                detail.setViolationId(savedReport.getId());
                detail.setViolationType(detailDTO.getViolationType());
                detail.setFineAmount(detailDTO.getFineAmount());
                violationDetailRepository.save(detail);
            }
        }

        return getViolationById(savedReport.getId());
    }

    @Transactional
    public MotorcycleViolationReportDTO updateViolation(Integer id, MotorcycleViolationReportDTO violationDTO) {
        MotorcycleViolationReport report = violationReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Violation report not found with id: " + id));

        // Cập nhật thông tin báo cáo
        if (violationDTO.getLicensePlate() != null) {
            if (!motorcycleRepository.existsByLicensePlate(violationDTO.getLicensePlate())) {
                throw new EntityNotFoundException(
                        "Motorcycle not found with license plate: " + violationDTO.getLicensePlate());
            }
            report.setLicensePlate(violationDTO.getLicensePlate());
        }

        if (violationDTO.getViolatorId() != null) {
            if (!personRepository.existsById(violationDTO.getViolatorId())) {
                throw new EntityNotFoundException("Person not found with id: " + violationDTO.getViolatorId());
            }
            report.setViolatorId(violationDTO.getViolatorId());
        }

        if (violationDTO.getOfficerId() != null) {
            report.setOfficerId(violationDTO.getOfficerId());
        }

        if (violationDTO.getReportTime() != null) {
            report.setReportTime(violationDTO.getReportTime());
        }

        if (violationDTO.getReportLocation() != null) {
            report.setReportLocation(violationDTO.getReportLocation());
        }

        if (violationDTO.getPenaltyType() != null) {
            report.setPenaltyType(violationDTO.getPenaltyType());
        }

        if (violationDTO.getResolutionDeadline() != null) {
            report.setResolutionDeadline(violationDTO.getResolutionDeadline());
        }

        violationReportRepository.save(report);

        // Cập nhật chi tiết vi phạm nếu có
        if (violationDTO.getViolationDetails() != null && !violationDTO.getViolationDetails().isEmpty()) {
            // Xóa chi tiết cũ
            List<MotorcycleViolationDetail> existingDetails = violationDetailRepository.findByViolationId(id);
            violationDetailRepository.deleteAll(existingDetails);

            // Thêm chi tiết mới
            for (MotorcycleViolationDetailDTO detailDTO : violationDTO.getViolationDetails()) {
                MotorcycleViolationDetail detail = new MotorcycleViolationDetail();
                detail.setViolationId(id);
                detail.setViolationType(detailDTO.getViolationType());
                detail.setFineAmount(detailDTO.getFineAmount());
                violationDetailRepository.save(detail);
            }
        }

        return getViolationById(id);
    }

    @Transactional
    public void deleteViolation(Integer id) {
        if (!violationReportRepository.existsById(id)) {
            throw new EntityNotFoundException("Violation report not found with id: " + id);
        }

        // Xóa chi tiết vi phạm trước
        List<MotorcycleViolationDetail> details = violationDetailRepository.findByViolationId(id);
        violationDetailRepository.deleteAll(details);

        // Xóa báo cáo vi phạm
        violationReportRepository.deleteById(id);
    }

    private MotorcycleViolationReportDTO convertToDTO(MotorcycleViolationReport report) {
        MotorcycleViolationReportDTO dto = new MotorcycleViolationReportDTO();
        dto.setId(report.getId());
        dto.setLicensePlate(report.getLicensePlate());
        dto.setViolatorId(report.getViolatorId());
        dto.setOfficerId(report.getOfficerId());
        dto.setReportTime(report.getReportTime());
        dto.setReportLocation(report.getReportLocation());
        dto.setPenaltyType(report.getPenaltyType());
        dto.setResolutionDeadline(report.getResolutionDeadline());

        // Lấy thông tin bổ sung
        if (report.getViolator() != null) {
            dto.setViolatorName(report.getViolator().getFullName());
        }

        if (report.getOfficer() != null) {
            dto.setOfficerName(report.getOfficer().getUsername());
        }

        if (report.getMotorcycle() != null) {
            dto.setMotorcycleBrand(report.getMotorcycle().getBrand());
            dto.setMotorcycleColor(report.getMotorcycle().getColor());
        }

        // Lấy chi tiết vi phạm
        List<MotorcycleViolationDetail> details = violationDetailRepository.findByViolationId(report.getId());
        List<MotorcycleViolationDetailDTO> detailDTOs = new ArrayList<>();

        for (MotorcycleViolationDetail detail : details) {
            MotorcycleViolationDetailDTO detailDTO = new MotorcycleViolationDetailDTO();
            detailDTO.setId(detail.getId());
            detailDTO.setViolationId(detail.getViolationId());
            detailDTO.setViolationType(detail.getViolationType());
            detailDTO.setFineAmount(detail.getFineAmount());
            detailDTOs.add(detailDTO);
        }

        dto.setViolationDetails(detailDTOs);

        return dto;
    }
}