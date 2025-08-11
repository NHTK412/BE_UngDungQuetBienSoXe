package com.example.licenseplate.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.licenseplate.model.FcmToken;
import com.example.licenseplate.repository.FcmTokenRepository;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/fcm-token")
public class FcmTokenController {

    private final FcmTokenRepository fcmTokenRepository;

    public FcmTokenController(FcmTokenRepository fcmTokenRepository) {
        this.fcmTokenRepository = fcmTokenRepository;
    }

    @PostMapping("/token")
    public ResponseEntity<?> postCreateFcmToken(@RequestBody FcmToken fcmToken) {
        try {
            FcmToken result = fcmTokenRepository.save(fcmToken);
            return ResponseEntity.ok(result);
           
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(

                    "message", "Error",
                    "error", e.getMessage()));
        }
    }
}
