package com.example.licenseplate.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.licenseplate.dto.UserLocationRequest;
import com.example.licenseplate.model.UserLocation;
import com.example.licenseplate.repository.UserLocationRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/user-location")
public class UserLocationController {

    public final UserLocationRepository userLocationRepository;

    public UserLocationController(UserLocationRepository userLocationRepository) {
        this.userLocationRepository = userLocationRepository;
    }

    @PutMapping()
    public void  updateLocation(@RequestBody UserLocationRequest userLocationReq) {

        UserLocation userLocation = userLocationRepository.findByAccountId(userLocationReq.getAccountId());

        userLocation.setLatitude(new BigDecimal(Double.toString(userLocationReq.getLatitude())));
        userLocation.setLongitude(new BigDecimal(Double.toString(userLocationReq.getLongitude())));

        userLocationRepository.save(userLocation);

        // return ResponseEntity.ok(Map.of(
                // "mess", "ok"));
    }

}
