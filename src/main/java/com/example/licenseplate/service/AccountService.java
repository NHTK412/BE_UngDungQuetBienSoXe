package com.example.licenseplate.service;

import com.example.licenseplate.model.Account;
import com.example.licenseplate.repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    public Optional<Account> getAccountById(String id) {
        return accountRepository.findById(id);
    }
    public Account createAccount(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }
    public Account updateAccount(String id, Account updatedAccount) {
        return accountRepository.findById(id).map(account -> {
            account.setUsername(updatedAccount.getUsername());
            account.setEmail(updatedAccount.getEmail());
            if (updatedAccount.getPassword() != null && !updatedAccount.getPassword().isEmpty()) {
                account.setPassword(passwordEncoder.encode(updatedAccount.getPassword()));
            }
            account.setRole(updatedAccount.getRole());
            return accountRepository.save(account);
        }).orElseThrow(() -> new RuntimeException("Account not found"));
    }
    public void deleteAccount(String id) {
        accountRepository.deleteById(id);
    }

    
}
