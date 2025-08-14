package com.example.licenseplate.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GoongDistanceMatrixService {

    @Value("${goong.map.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public GoongDistanceMatrixService() {
        this.restTemplate = new RestTemplate();
    }

    public Double getDistanceMatrix(String origins, String destinations, String vehicle) {
        try {
            // Tạo URL với proper encoding
            String url = UriComponentsBuilder
                    .fromHttpUrl("https://rsapi.goong.io/DistanceMatrix")
                    .queryParam("origins", origins)
                    .queryParam("destinations", destinations)
                    .queryParam("vehicle", vehicle)
                    .queryParam("api_key", apiKey)
                    .build()
                    .toUriString();

            log.info("Calling Goong API with URL: {}", url);

            // Thêm headers giống như Postman
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            headers.set("Accept", "application/json, text/plain, */*");
            headers.set("Accept-Language", "en-US,en;q=0.9,vi;q=0.8");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<GoongDistanceMatrixResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    GoongDistanceMatrixResponse.class);

            GoongDistanceMatrixResponse body = response.getBody();

            if (body != null && body.getRows() != null && !body.getRows().isEmpty()) {
                GoongDistanceMatrixResponse.Row row = body.getRows().get(0);
                if (row.getElements() != null && !row.getElements().isEmpty()) {
                    GoongDistanceMatrixResponse.Element element = row.getElements().get(0);
                    if (element.getStatus().equals("OK") && element.getDistance() != null) {
                        // Convert từ meters sang kilometers
                        double distanceInKm = element.getDistance().getValue() / 1000.0;
                        log.info("Distance calculated: {} km", distanceInKm);
                        return distanceInKm;
                    }
                }
            }

            log.warn("No valid distance found in response");
            return null;

        } catch (Exception e) {
            log.error("Error calling Goong API: {}", e.getMessage(), e);
            return null;
        }
    }

    // Inner classes for response mapping
    public static class GoongDistanceMatrixResponse {
        private java.util.List<Row> rows;
        private String status;

        // Getters and setters
        public java.util.List<Row> getRows() {
            return rows;
        }

        public void setRows(java.util.List<Row> rows) {
            this.rows = rows;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public static class Row {
            private java.util.List<Element> elements;

            public java.util.List<Element> getElements() {
                return elements;
            }

            public void setElements(java.util.List<Element> elements) {
                this.elements = elements;
            }
        }

        public static class Element {
            private Distance distance;
            private Duration duration;
            private String status;

            public Distance getDistance() {
                return distance;
            }

            public void setDistance(Distance distance) {
                this.distance = distance;
            }

            public Duration getDuration() {
                return duration;
            }

            public void setDuration(Duration duration) {
                this.duration = duration;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }

        public static class Distance {
            private String text;
            private int value; // in meters

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        public static class Duration {
            private String text;
            private int value; // in seconds

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }
    }
}