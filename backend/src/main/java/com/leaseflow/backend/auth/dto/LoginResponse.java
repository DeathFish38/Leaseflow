package com.leaseflow.backend.auth.dto;

public record LoginResponse(
        Long userId,
        String email,
        String message) {
}
