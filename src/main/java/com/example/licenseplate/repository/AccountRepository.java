package com.example.licenseplate.repository;

import com.example.licenseplate.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT p, a FROM Account a JOIN Person p ON a.id = p.id " +
            "WHERE (:role IS NULL OR a.role = :role)")
    java.util.List<Object[]> findStaff(@Param("role") String role);

}
