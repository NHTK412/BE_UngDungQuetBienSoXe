package com.example.licenseplate.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "driving_license")
@Data
@NoArgsConstructor
public class DrivingLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "license_number", nullable = false, unique = true)
    private String licenseNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false)
    private Person person;

    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "license_class", nullable = false)
    private String licenseClass;

    @Column(name = "place_of_issue")
    private String placeOfIssue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.VALID;

    public enum Status {
        VALID,
        EXPIRED,
        SUSPENDED
    }
}
