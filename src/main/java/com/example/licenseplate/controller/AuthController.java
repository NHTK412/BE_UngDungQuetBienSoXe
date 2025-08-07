package com.example.licenseplate.controller;

import com.example.licenseplate.dto.JwtResponse;
import com.example.licenseplate.dto.LoginHistoryRequest;
import com.example.licenseplate.dto.LogoutResponse;
import com.example.licenseplate.dto.SignInRequest;
import com.example.licenseplate.dto.SignUpRequest;
import com.example.licenseplate.model.Account;
import com.example.licenseplate.model.LoginHistory;
import com.example.licenseplate.repository.AccountRepository;
import com.example.licenseplate.service.LoginHistoryService;
import com.example.licenseplate.service.UserDetailsImpl;
import com.example.licenseplate.util.JwtUtil;
import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // New
    // update________________________________________________________________________________
    private final LoginHistoryService loginHistoryService;

    public AuthController(AuthenticationManager authenticationManager,
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            LoginHistoryService loginHistoryService) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.loginHistoryService = loginHistoryService;
    }
    // New
    // update________________________________________________________________________________

    // public AuthController(AuthenticationManager authenticationManager,
    // AccountRepository accountRepository,
    // PasswordEncoder passwordEncoder,
    // JwtUtil jwtUtil) {
    // this.authenticationManager = authenticationManager;
    // this.accountRepository = accountRepository;
    // this.passwordEncoder = passwordEncoder;
    // this.jwtUtil = jwtUtil;
    // }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest request, HttpServletRequest httpRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());
        JwtResponse response = new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest request) {
        if (accountRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken");
        }
        if (accountRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already in use");
        }
        Account account = new Account();
        account.setId(request.getId());
        account.setUsername(request.getUsername());
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setRole("USER");
        accountRepository.save(account);
        return ResponseEntity.ok("User registered successfully");
    }

    // New endpoint to handle login
    // history___________________________________________________________________________

    @PostMapping("/login-history")
    public ResponseEntity<?> postLoginHistory(@Valid @RequestBody LoginHistoryRequest request) {
        try {
            LoginHistory savedHistory = loginHistoryService.createLoginHistory(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedHistory);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save login history: " + e.getMessage());
        }
    }

    @GetMapping("/login-history/{accountId}")
    public ResponseEntity<?> getLoginHistory(@PathVariable String accountId) {
        try {
            List<LoginHistory> loginHistories = loginHistoryService.getLoginHistoryByAccountId(accountId);
            return ResponseEntity.ok(loginHistories);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve login history: " + e.getMessage());
        }
    }

    // @PatchMapping("/logout/{accountId}")
    @PatchMapping("/logout/{accountId}")
    public ResponseEntity<LogoutResponse> logout(@PathVariable String accountId) {
        try {
            LoginHistory loggedOutSession = loginHistoryService.logoutUser(accountId);
            return ResponseEntity.ok(new LogoutResponse(1, "Đã ghi nhận đăng xuất thành công")); // Trả về json 
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LogoutResponse(0, e.getMessage())); // Trả về json 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LogoutResponse(0, "Không thể ghi nhận đăng xuất: " + e.getMessage())); // Trả về json 
        }
    }

    // New endpoint to handle login
    // history___________________________________________________________________________
}
