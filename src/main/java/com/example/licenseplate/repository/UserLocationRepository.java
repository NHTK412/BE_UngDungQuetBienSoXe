package com.example.licenseplate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.licenseplate.dto.UserLocationResponse;
import com.example.licenseplate.model.UserLocation;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {
    UserLocation findByAccountId(String accountId);

    // List<UserLocation> getAllUserLocation();

   @Query(value = "SELECT ul.id, ul.latitude, ul.longitude, ul.online, a.username, a.email, p.full_name " +
               "FROM user_location ul " +
               "JOIN account a ON ul.account_id = a.id " +
               "JOIN person p ON a.id = p.id", nativeQuery = true)
List<Object[]> findAllUserLocationsWithAccountAndPerson();


}