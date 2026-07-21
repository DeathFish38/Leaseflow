package com.leaseflow.backend.auth.dto;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName) {
}
