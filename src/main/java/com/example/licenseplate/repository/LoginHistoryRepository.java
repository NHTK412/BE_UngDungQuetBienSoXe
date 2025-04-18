// File này được chỉnh sửa vào ngày 18.04.2025

package com.example.licenseplate.repository;

import com.example.licenseplate.model.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Integer> {
    List<LoginHistory> findByAccountId(String accountId); // Mới thêm vào

        
    Optional<LoginHistory> findTopByAccountIdAndLogoutTimeIsNullOrderByLoginTimeDesc(String accountId); // Mới thêm vào

    
}
