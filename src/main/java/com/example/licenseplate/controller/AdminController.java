package com.example.licenseplate.controller;

import com.example.licenseplate.dto.AdminSignUpRequest;
import com.example.licenseplate.dto.AdminUpdateRequest;
import com.example.licenseplate.model.ERole;
import com.example.licenseplate.model.Role;
import com.example.licenseplate.model.User;
import com.example.licenseplate.repository.RoleRepository;
import com.example.licenseplate.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Lấy danh sách các tài khoản admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllAdmins() {
        List<User> admins = userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN))
                .collect(Collectors.toList());
        return ResponseEntity.ok(admins);
    }

    // Tạo tài khoản admin mới
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody AdminSignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already in use");
        }
        User admin = new User();
        admin.setUsername(signUpRequest.getUsername());
        admin.setEmail(signUpRequest.getEmail());
        admin.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        // Thiết lập role là ROLE_ADMIN
        Set<Role> roles = new HashSet<>();
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role ADMIN not found"));
        roles.add(adminRole);
        admin.setRoles(roles);

        userRepository.save(admin);
        return ResponseEntity.ok("Admin registered successfully");
    }

    // Cập nhật thông tin tài khoản admin
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id, @Valid @RequestBody AdminUpdateRequest updateRequest) {
        Optional<User> adminOpt = userRepository.findById(id);
        if (!adminOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User admin = adminOpt.get();
        // Kiểm tra nếu tài khoản không có role ADMIN
        if (admin.getRoles().stream().noneMatch(role -> role.getName() == ERole.ROLE_ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not an admin account");
        }
        admin.setUsername(updateRequest.getUsername());
        admin.setEmail(updateRequest.getEmail());
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isBlank()) {
            admin.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }
        userRepository.save(admin);
        return ResponseEntity.ok("Admin updated successfully");
    }

    // Xóa tài khoản admin
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        Optional<User> adminOpt = userRepository.findById(id);
        if (!adminOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User admin = adminOpt.get();
        if (admin.getRoles().stream().noneMatch(role -> role.getName() == ERole.ROLE_ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not an admin account");
        }
        userRepository.delete(admin);
        return ResponseEntity.ok("Admin deleted successfully");
    }
}
