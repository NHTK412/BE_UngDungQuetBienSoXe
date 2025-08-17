package com.example.licenseplate.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.licenseplate.dto.UserLocationRequest;
import com.example.licenseplate.dto.UserLocationResponse;
import com.example.licenseplate.model.UserLocation;
import com.example.licenseplate.repository.UserLocationRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/user-location")
public class UserLocationController {

    public final UserLocationRepository userLocationRepository;

    public UserLocationController(UserLocationRepository userLocationRepository) {
        this.userLocationRepository = userLocationRepository;
    }

    @PutMapping()
    public void updateLocation(@RequestBody UserLocationRequest userLocationReq) {

        UserLocation userLocation = userLocationRepository.findByAccountId(userLocationReq.getAccountId());

        userLocation.setLatitude(new BigDecimal(Double.toString(userLocationReq.getLatitude())));
        userLocation.setLongitude(new BigDecimal(Double.toString(userLocationReq.getLongitude())));

        userLocationRepository.save(userLocation);

        // return ResponseEntity.ok(Map.of(
        // "mess", "ok"));
    }

    @GetMapping()
    public ResponseEntity<?> getAllUserLocation() {
        // List<UserLocationResponse> userLocations =
        // userLocationRepository.findAll().stream()
        // .map(ul -> new UserLocationResponse(
        // ul.getId(),
        // ul.getLatitude(),
        // ul.getLongitude(),
        // ul.getOnline(),
        // ul.getAccount().getUsername()
        // ))
        // .toList();

        List<Object[]> results = userLocationRepository.findAllUserLocationsWithAccountAndPerson();
        List<UserLocationResponse> response = results.stream()
                .map(r -> new UserLocationResponse(
                        ((Number) r[0]).longValue(),
                        (BigDecimal) r[1],
                        (BigDecimal) r[2],
                        (Boolean) r[3],
                        (String) r[4],
                        (String) r[5],
                        (String) r[6]))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);

    }

}
