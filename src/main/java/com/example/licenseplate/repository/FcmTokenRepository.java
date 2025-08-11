package com.example.licenseplate.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.licenseplate.model.FcmToken;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long>{

}
