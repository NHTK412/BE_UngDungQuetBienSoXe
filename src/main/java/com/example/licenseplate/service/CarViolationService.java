// New File
// CarViolationService.java

package com.example.licenseplate.service;

import com.example.licenseplate.dto.CarViolationDetailDTO;
import com.example.licenseplate.dto.CarViolationReportDTO;
import com.example.licenseplate.model.CarViolationDetail;
import com.example.licenseplate.model.CarViolationReport;
import com.example.licenseplate.repository.CarRepository;
import com.example.licenseplate.repository.CarViolationDetailRepository;
import com.example.licenseplate.repository.CarViolationReportRepository;
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
public class CarViolationService {

    @Autowired
    private CarViolationReportRepository violationReportRepository;
    
    @Autowired
    private CarViolationDetailRepository violationDetailRepository;
    
    @Autowired
    private CarRepository carRepository;
    
    @Autowired
    private PersonRepository personRepository;

    public List<CarViolationReportDTO> getAllViolations() {
        List<CarViolationReport> reports = violationReportRepository.findAll();
        return reports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CarViolationReportDTO getViolationById(Integer id) {
        CarViolationReport report = violationReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Violation report not found with id: " + id));
        return convertToDTO(report);
    }

    public List<CarViolationReportDTO> getViolationsByLicensePlate(String licensePlate) {
        List<CarViolationReport> reports = violationReportRepository.findByLicensePlate(licensePlate);
        return reports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CarViolationReportDTO> getViolationsByViolatorId(String violatorId) {
        List<CarViolationReport> reports = violationReportRepository.findByViolatorId(violatorId);
        return reports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CarViolationReportDTO createViolation(CarViolationReportDTO violationDTO) {
        // Kiểm tra xem license plate có tồn tại
        if (!carRepository.existsByLicensePlate(violationDTO.getLicensePlate())) {
            throw new EntityNotFoundException("Car not found with license plate: " + violationDTO.getLicensePlate());
        }
        
        // Kiểm tra người vi phạm có tồn tại
        if (!personRepository.existsById(violationDTO.getViolatorId())) {
            throw new EntityNotFoundException("Person not found with id: " + violationDTO.getViolatorId());
        }

        // Tạo mới báo cáo vi phạm
        CarViolationReport report = new CarViolationReport();
        report.setLicensePlate(violationDTO.getLicensePlate());
        report.setViolatorId(violationDTO.getViolatorId());
        report.setOfficerId(violationDTO.getOfficerId());
        report.setReportTime(violationDTO.getReportTime() != null ? violationDTO.getReportTime() : LocalDateTime.now());
        report.setReportLocation(violationDTO.getReportLocation());
        report.setPenaltyType(violationDTO.getPenaltyType());
        report.setResolutionDeadline(violationDTO.getResolutionDeadline());
        
        CarViolationReport savedReport = violationReportRepository.save(report);
        
        // Lưu chi tiết vi phạm nếu có
        if (violationDTO.getViolationDetails() != null && !violationDTO.getViolationDetails().isEmpty()) {
            for (CarViolationDetailDTO detailDTO : violationDTO.getViolationDetails()) {
                CarViolationDetail detail = new CarViolationDetail();
                detail.setViolationId(savedReport.getId());
                detail.setViolationType(detailDTO.getViolationType());
                detail.setFineAmount(detailDTO.getFineAmount());
                violationDetailRepository.save(detail);
            }
        }
        
        return getViolationById(savedReport.getId());
    }

    @Transactional
    public CarViolationReportDTO updateViolation(Integer id, CarViolationReportDTO violationDTO) {
        CarViolationReport report = violationReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Violation report not found with id: " + id));
        
        // Cập nhật thông tin báo cáo
        if (violationDTO.getLicensePlate() != null) {
            if (!carRepository.existsByLicensePlate(violationDTO.getLicensePlate())) {
                throw new EntityNotFoundException("Car not found with license plate: " + violationDTO.getLicensePlate());
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
            List<CarViolationDetail> existingDetails = violationDetailRepository.findByViolationId(id);
            violationDetailRepository.deleteAll(existingDetails);
            
            // Thêm chi tiết mới
            for (CarViolationDetailDTO detailDTO : violationDTO.getViolationDetails()) {
                CarViolationDetail detail = new CarViolationDetail();
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
        List<CarViolationDetail> details = violationDetailRepository.findByViolationId(id);
        violationDetailRepository.deleteAll(details);
        
        // Xóa báo cáo vi phạm
        violationReportRepository.deleteById(id);
    }
    
    private CarViolationReportDTO convertToDTO(CarViolationReport report) {
        CarViolationReportDTO dto = new CarViolationReportDTO();
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
        
        if (report.getCar() != null) {
            dto.setCarBrand(report.getCar().getBrand());
            dto.setCarColor(report.getCar().getColor());
        }
        
        // Lấy chi tiết vi phạm
        List<CarViolationDetail> details = violationDetailRepository.findByViolationId(report.getId());
        List<CarViolationDetailDTO> detailDTOs = new ArrayList<>();
        
        for (CarViolationDetail detail : details) {
            CarViolationDetailDTO detailDTO = new CarViolationDetailDTO();
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