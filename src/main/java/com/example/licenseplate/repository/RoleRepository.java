package com.example.licenseplate.repository;

import com.example.licenseplate.model.ERole;
import com.example.licenseplate.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
