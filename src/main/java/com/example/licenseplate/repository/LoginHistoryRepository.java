package com.example.licenseplate.repository;

import com.example.licenseplate.model.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Integer> {
}
