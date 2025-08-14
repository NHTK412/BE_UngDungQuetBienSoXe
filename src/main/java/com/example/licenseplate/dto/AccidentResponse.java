package com.example.licenseplate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccidentResponse {
    private Integer accidentId;
    private String roadName;
    private Integer cameraId;
    private LocalDateTime timestamp;
    private String accidentType;
    private String imageUrl;
    private List<responderInfo> responder;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class responderInfo {
        private String unitId;
        private String unitType;
        private String status;
    }
}
