package com.example.licenseplate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import com.example.licenseplate.dto.DistanceMatrixResponse;

@Slf4j
@Service
public class GoongDistanceMatrixService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${goong.map.api.key}")
    private String apiKey;

    public Double getDistanceMatrix(String origin, String destination, String vehicle) {
        try {
            if (vehicle == null || vehicle.isEmpty()) {
                vehicle = "car"; // Giá trị mặc định
            }

            // Encode tham số để tránh lỗi URL
            String encodedOrigin = URLEncoder.encode(origin, StandardCharsets.UTF_8);
            String encodedDestination = URLEncoder.encode(destination, StandardCharsets.UTF_8);

            String url = String.format(
                "https://rsapi.goong.io/DistanceMatrix?origins=%s&destinations=%s&vehicle=%s&api_key=%s",
                encodedOrigin, encodedDestination, vehicle, apiKey
            );

            log.info("Calling Goong API with URL: {}", url);

            DistanceMatrixResponse response = restTemplate.getForObject(url, DistanceMatrixResponse.class);

            if (response == null || response.getRows() == null || response.getRows().isEmpty()) {
                return null;
            }

            DistanceMatrixResponse.Element element = response.getRows().get(0).getElements().get(0);
            if (element == null || element.getDistance() == null) {
                return null;
            }

            // Lấy khoảng cách (mét → km)
            return element.getDistance().getValue() / 1000.0;

        } catch (Exception e) {
            log.error("Error calling Goong API: {}", e.getMessage(), e);
            return null;
        }
    }
}
