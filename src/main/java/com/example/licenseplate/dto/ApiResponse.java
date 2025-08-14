package com.example.licenseplate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String status; // success, error
    private T data;

    // Constructor for error response
    public ApiResponse(String status) {
        this.status = status;
        this.data = null;
    }

    // Static factory methods for cleaner code
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, null);
    }
}