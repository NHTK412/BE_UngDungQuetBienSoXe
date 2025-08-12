package com.example.licenseplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.licenseplate.model.UserLocation;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {
    UserLocation findByAccountId(String accountId);
}