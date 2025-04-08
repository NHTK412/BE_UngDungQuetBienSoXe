package com.example.licenseplate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Entity
@Table(name = "face_data")
@Data
@NoArgsConstructor
public class FaceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Column(name = "tracking_id")
    private Integer trackingId;

    @Column(name = "bounding_box_left")
    private Float boundingBoxLeft;

    @Column(name = "bounding_box_top")
    private Float boundingBoxTop;

    @Column(name = "bounding_box_right")
    private Float boundingBoxRight;

    @Column(name = "bounding_box_bottom")
    private Float boundingBoxBottom;

    @Column(name = "rotation_x")
    private Float rotationX;

    @Column(name = "rotation_y")
    private Float rotationY;

    @Column(name = "rotation_z")
    private Float rotationZ;

    @Column(name = "right_eye_open_probability")
    private Float rightEyeOpenProbability;

    @Column(name = "left_eye_open_probability")
    private Float leftEyeOpenProbability;

    @Column(name = "smiling_probability")
    private Float smilingProbability;

    @Column(name = "left_eye_x")
    private Float leftEyeX;

    @Column(name = "left_eye_y")
    private Float leftEyeY;

    @Column(name = "right_eye_x")
    private Float rightEyeX;

    @Column(name = "right_eye_y")
    private Float rightEyeY;

    @Column(name = "nose_x")
    private Float noseX;

    @Column(name = "nose_y")
    private Float noseY;

    @Column(name = "mouth_left_x")
    private Float mouthLeftX;

    @Column(name = "mouth_left_y")
    private Float mouthLeftY;

    @Column(name = "mouth_right_x")
    private Float mouthRightX;

    @Column(name = "mouth_right_y")
    private Float mouthRightY;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;
}
