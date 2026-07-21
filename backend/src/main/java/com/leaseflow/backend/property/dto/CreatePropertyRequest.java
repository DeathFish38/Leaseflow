package com.leaseflow.backend.property.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record CreatePropertyRequest(
        @NotBlank String nickname,
        @NotBlank String addressLine1,
        @NotBlank String suburb,
        @NotBlank String state,
        @NotBlank String postcode,
        LocalDate moveInDate, 
        LocalDate moveOutDate
    ) {}
