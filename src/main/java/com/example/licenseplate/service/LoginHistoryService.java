// File mới
// LoginHistoryService.java

package com.example.licenseplate.service;

import com.example.licenseplate.dto.LoginHistoryRequest;
import com.example.licenseplate.model.Account;
import com.example.licenseplate.model.LoginHistory;
import com.example.licenseplate.repository.AccountRepository;
import com.example.licenseplate.repository.LoginHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LoginHistoryService {

    private final LoginHistoryRepository loginHistoryRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public LoginHistoryService(LoginHistoryRepository loginHistoryRepository,
            AccountRepository accountRepository) {
        this.loginHistoryRepository = loginHistoryRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public LoginHistory createLoginHistory(LoginHistoryRequest request) {
        // Update 10:25 ngày 18 tháng 4 năm 2025
        // _________________________________________________________________________________________
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Account ID " + request.getAccountId() + " does not exist"));

        // Nếu trạng thái đăng nhập là thành công → cập nhật lastLogin
        if ("SUCCESS".equalsIgnoreCase(request.getLoginStatus())) {
            account.setLastLogin(LocalDateTime.now());
            accountRepository.save(account);
        }

        // Tạo đối tượng lịch sử đăng nhập
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setAccountId(request.getAccountId());
        loginHistory.setLoginTime(LocalDateTime.now());
        loginHistory.setIpAddress(request.getIpAddress());
        loginHistory.setDeviceInfo(request.getDeviceInfo());
        loginHistory.setLoginStatus(request.getLoginStatus());

        // Lưu lịch sử đăng nhập
        return loginHistoryRepository.save(loginHistory);
        // _________________________________________________________________________________________

    }

    @Transactional(readOnly = true)
    public List<LoginHistory> getLoginHistoryByAccountId(String accountId) {
        // Xác thực tài khoản tồn tại
        if (!accountRepository.existsById(accountId)) {
            throw new EntityNotFoundException("Account ID " + accountId + " does not exist");
        }

        // Lấy toàn bộ lịch sử đăng nhập của tài khoản
        return loginHistoryRepository.findByAccountId(accountId);
    }

    @Transactional
    public LoginHistory logoutUser(String accountId) {
        // Tìm lần đăng nhập gần nhất chưa có thời gian đăng xuất
        Optional<LoginHistory> mostRecentLoginOpt = loginHistoryRepository
                .findTopByAccountIdAndLogoutTimeIsNullOrderByLoginTimeDesc(accountId);

        if (mostRecentLoginOpt.isEmpty()) {
            throw new EntityNotFoundException(
                    "Không tìm thấy phiên đăng nhập đang hoạt động cho account ID " + accountId);
        }

        // Cập nhật thời gian đăng xuất
        LoginHistory mostRecentLogin = mostRecentLoginOpt.get();
        mostRecentLogin.setLogoutTime(LocalDateTime.now());
        return loginHistoryRepository.save(mostRecentLogin);
    }
}