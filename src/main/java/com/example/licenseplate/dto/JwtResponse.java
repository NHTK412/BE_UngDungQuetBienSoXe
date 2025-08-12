package com.example.licenseplate.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String id; // Account id (CCCD)
    private String username;
    private List<String> roles;

        // NEW UPDATE 9H46 NGAY 9/8/2025
        private String email; 
        // NEW UPDATE 9H46 NGAY 9/8/2025



    public JwtResponse(String token, String id, String username,String email ,List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.roles = roles;

        this.email = email;
    }
}
