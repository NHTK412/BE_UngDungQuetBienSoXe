package com.example.licenseplate.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.licenseplate.dto.FcmTokenRequest;
import com.example.licenseplate.model.Account;
import com.example.licenseplate.model.FcmToken;
import com.example.licenseplate.repository.AccountRepository;
import com.example.licenseplate.repository.FcmTokenRepository;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/fcm-token")
public class FcmTokenController {

    private final FcmTokenRepository fcmTokenRepository;

    private final AccountRepository accountRepository;

    public FcmTokenController(FcmTokenRepository fcmTokenRepository, AccountRepository accountRepository) {
        this.fcmTokenRepository = fcmTokenRepository;
        this.accountRepository = accountRepository;
    }

    @PostMapping("/token")
    public ResponseEntity<?> postCreateFcmToken(@RequestBody FcmTokenRequest fcmTokenReq) {
        try {

            FcmToken checkToken = fcmTokenRepository.findByAccountId(fcmTokenReq.getAccountId());
            if (checkToken == null)
            {
                Account account = accountRepository.getById(fcmTokenReq.getAccountId());


                FcmToken fcmToken = new FcmToken(null, fcmTokenReq.getToken(), account);
                FcmToken newFcmToken = fcmTokenRepository.save(fcmToken);
                return ResponseEntity.ok(Map.of(
                    "message", "Token saved successfully",
                    "data", newFcmToken));
            }
            checkToken.setToken(fcmTokenReq.getToken());
            
            FcmToken newFcmToken = fcmTokenRepository.save(checkToken);
             return ResponseEntity.ok(Map.of(
                    "message", "Token saved successfully",
                    "data", newFcmToken));

            // FcmToken existingToken = fcmTokenRepository.findByAccountId(fcmToken.getAccount().getId());
            // if (existingToken != null) {
            //     // Nếu đã có token, cập nhật token mới
            //     existingToken.setToken(fcmToken.getToken());
            //     fcmToken = existingToken;
            // } else {
            //     // Nếu chưa có token, tạo mới
            //     fcmToken.setId(null); // Đặt ID null để tạo mới
            // }

            // Lưu token vào cơ sở dữ liệu
            // FcmToken result = fcmTokenRepository.save(fcmToken);
            // return ResponseEntity.ok(Map.of(
            //         "message", "Token saved successfully",
            //         "data", result));



            // FcmToken result = fcmTokenRepository.save(fcmToken);
            // return ResponseEntity.ok(result);
           
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(

                    "message", "Error",
                    "error", e.getMessage()));
        }
    }
}
