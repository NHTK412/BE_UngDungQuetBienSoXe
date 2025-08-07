package com.example.licenseplate.controller;

import com.example.licenseplate.model.Person;
import com.example.licenseplate.service.PersonService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller xử lý các yêu cầu nhận dạng khuôn mặt
 * Đảm bảo người dùng đã xác thực trước khi truy cập các API
 */
@RestController
@RequestMapping("/api/face-recognition")
@PreAuthorize("isAuthenticated()")
public class FaceRecognitionController {

    private final PersonService personService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // URL dịch vụ Python lấy từ cấu hình application.yml
    @Value("${python.service.url}")
    private String pythonServiceUrl;

    /**
     * Constructor với dependency injection
     */
    public FaceRecognitionController(PersonService personService, RestTemplate restTemplate,
            ObjectMapper objectMapper) {
        this.personService = personService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * API nhận diện khuôn mặt từ ảnh
     * 
     * @param imageFile Ảnh chứa khuôn mặt cần nhận diện
     * @return Thông tin người dùng nếu nhận diện thành công
     */
    @PostMapping("/identify")
    public ResponseEntity<?> identifyPersonByFace(@RequestParam("image") MultipartFile imageFile) {
        try {
            // 1. Kiểm tra tính hợp lệ của file ảnh
            if (imageFile.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload an image file");
            }

            // 2. Chuyển tiếp ảnh đến dịch vụ Python để nhận diện
            String cccdId = forwardImageToPythonService(imageFile);
            if (cccdId == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No person identified in the image");
            }

            // 3. Truy vấn cơ sở dữ liệu để lấy thông tin người dùng dựa trên số CCCD
            Optional<Person> person = personService.getPersonById(cccdId);
            if (person.isPresent()) {
                return ResponseEntity.ok(person.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Person with CCCD " + cccdId + " not found in database");
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing image: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Phương thức gửi ảnh đến dịch vụ Python và nhận về số CCCD
     * 
     * @param imageFile File ảnh cần gửi
     * @return Số CCCD nếu nhận diện thành công, null nếu thất bại
     */
    private String forwardImageToPythonService(MultipartFile imageFile) throws IOException {
        // 1. Chuẩn bị HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 2. Chuẩn bị body của request với file ảnh
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        // Tạo resource từ file và thêm vào body với tên trường là "file"
        body.add("file", new MultipartInputStreamFileResource(
                imageFile.getInputStream(),
                imageFile.getOriginalFilename()));

        // 3. Tạo HTTP request
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 4. Gửi request đến dịch vụ Python
        ResponseEntity<String> response = restTemplate.postForEntity(
                pythonServiceUrl + "/v1/match-image",
                requestEntity,
                String.class);

        // 5. Xử lý JSON response
        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());

            // Kiểm tra trạng thái trả về
            if (root.has("status") && "success".equals(root.get("status").asText())) {
                // Kiểm tra xem có trường name (định danh CCCD) không
                if (root.has("name")) {
                    String cccdId = root.get("name").asText();

                    // Thêm log để debug
                    if (root.has("confidence")) {
                        double confidence = root.get("confidence").asDouble();
                        System.out.println("Detected face with confidence: " + confidence);

                        // Giảm ngưỡng xuống 0.6 hoặc tùy chỉnh theo nhu cầu của bạn
                        if (confidence < 0.6) {
                            System.out.println("Confidence too low, below threshold");
                            return null;
                        }
                    }

                    return cccdId;
                }
            }
        }

        // Log toàn bộ phản hồi để debug
        System.out.println("Python service response: " + response.getBody());
        return null;
    }

    /**
     * Lớp helper để xử lý MultipartFile như một Resource
     * Cần thiết cho việc gửi file trong multipart request
     */
    private static class MultipartInputStreamFileResource extends org.springframework.core.io.InputStreamResource {
        private final String filename;

        public MultipartInputStreamFileResource(java.io.InputStream inputStream, String filename) {
            super(inputStream);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return this.filename;
        }

        @Override
        public long contentLength() {
            // Trả về -1 để tránh đọc stream hai lần
            return -1;
        }
    }
}