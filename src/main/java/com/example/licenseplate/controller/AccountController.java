package com.example.licenseplate.controller;

import com.example.licenseplate.dto.ResetPasswordRequest;
import com.example.licenseplate.dto.StaffResponse;
import com.example.licenseplate.model.Account;
import com.example.licenseplate.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Endpoint này chỉ admin mới dùng (xem tất cả tài khoản)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    // Endpoint này có thể sử dụng bởi bất kỳ tài khoản đã đăng nhập nào để xem thông tin của mình
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable String id) {
        return accountService.getAccountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint update (reset password) cho tài khoản; có thể dùng cho USER và ADMIN (cần logic kiểm tra quyền trong service nếu cần)
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/reset_password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            Account updatedAccount = accountService.updateAccount(request.getId(), new Account() {{
                setUsername(request.getUsername());
                setEmail(request.getEmail());
                setPassword(request.getPassword());
                // Giữ nguyên role; nếu cần cập nhật role, thêm logic tại service
            }});
            return ResponseEntity.ok("Account updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating account: " + e.getMessage());
        }
    }
    //them join account + person
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/staff")
    public ResponseEntity<List<StaffResponse>> getStaff(@RequestParam(required = false) String role) {
        List<StaffResponse> staff = accountService.getStaff(role);
        return ResponseEntity.ok(staff);
    }
}
