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

@RestController
@RequestMapping("/api/face-recognition")
@PreAuthorize("isAuthenticated()")
public class FaceRecognitionController {

    private final PersonService personService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${python.service.url}")
    private String pythonServiceUrl;

    public FaceRecognitionController(PersonService personService, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.personService = personService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/identify")
    public ResponseEntity<?> identifyPersonByFace(@RequestParam("image") MultipartFile imageFile) {
        try {
            // 1. Validate the uploaded file
            if (imageFile.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload an image file");
            }

            // 2. Forward the image to Python service
            String cccdId = forwardImageToPythonService(imageFile);
            if (cccdId == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No person identified in the image");
            }

            // 3. Query the database for person information using the CCCD
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

    private String forwardImageToPythonService(MultipartFile imageFile) throws IOException {
        // 1. Prepare HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 2. Prepare the request body with the image file
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        // Create a resource from the file and add it to the body
        body.add("image", new MultipartInputStreamFileResource(
                imageFile.getInputStream(),
                imageFile.getOriginalFilename()
        ));

        // 3. Create the HTTP request
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 4. Send the request to the Python service
        ResponseEntity<String> response = restTemplate.postForEntity(
                pythonServiceUrl + "/identify",
                requestEntity,
                String.class
        );

        // 5. Process the JSON response
        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());
            // Assuming the Python service returns JSON with a field "cccd"
            if (root.has("cccd")) {
                return root.get("cccd").asText();
            }
        }
        return null;
    }

    // Helper class to handle MultipartFile as a Resource
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
            // Returning -1 to avoid reading the stream twice
            return -1;
        }
    }
}