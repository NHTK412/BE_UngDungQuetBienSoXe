package com.example.licenseplate.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/api/images")
@PreAuthorize("isAuthenticated()")
public class ImageController {

    @Value("${app.image.directory}")
    private String imageDirectory;

    @Value("${app.image.accident}")
    private String imageAccidentPath;

    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) throws IOException {
        System.out.println("IMAGE " + imageDirectory);
        Path imagePath = Paths.get(imageDirectory + filename);
        if (!Files.exists(imagePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("File not found: " + filename).getBytes());
        }
        byte[] imageBytes = Files.readAllBytes(imagePath);
        String contentType = Files.probeContentType(imagePath);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(imageBytes);
    }

    @GetMapping("/accident/{filename}")
    public ResponseEntity<byte[]> getImageAccident(@PathVariable String filename) throws IOException {
        System.out.println("IMAGE " + imageAccidentPath);
        Path imagePath = Paths.get(imageAccidentPath + filename);
        if (!Files.exists(imagePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("File not found: " + filename).getBytes());
        }
        byte[] imageBytes = Files.readAllBytes(imagePath);
        String contentType = Files.probeContentType(imagePath);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(imageBytes);
    }
}