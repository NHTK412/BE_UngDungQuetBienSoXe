package com.example.licenseplate.dto;

import com.example.licenseplate.model.Account;
import com.example.licenseplate.model.Person;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class StaffResponse {

    private String id;
    private String fullName;
    private Date birthDate;
    private String gender;
    private String address;
    private String phoneNumber;
    private String facePath;
    private String username;
    private String email;
    private String role;
    private LocalDateTime lastLogin;

    public StaffResponse() {
    }

    public StaffResponse(String id, String fullName, Date birthDate, String gender,
            String address, String phoneNumber, String facePath, String username,
            String email, String role, LocalDateTime lastLogin) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.facePath = facePath;
        this.username = username;
        this.email = email;
        this.role = role;
        this.lastLogin = lastLogin;
    }

    public static StaffResponse from(Person p, Account a) {
        return new StaffResponse(
                p.getId(),
                p.getFullName(),
                p.getBirthDate(),
                p.getGender() != null ? p.getGender().toString() : null,
                p.getAddress(),
                p.getPhoneNumber(),
                p.getFacePath(),
                a.getUsername(),
                a.getEmail(),
                a.getRole(),
                a.getLastLogin());
    }
}