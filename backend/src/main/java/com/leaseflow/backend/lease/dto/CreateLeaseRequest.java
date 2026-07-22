package com.leaseflow.backend.lease.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.leaseflow.backend.lease.entity.PaymentFrequency;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CreateLeaseRequest(
                @NotNull LocalDate leaseStart,
                @NotNull LocalDate leaseEnd,
                @NotNull @DecimalMin("0.01") BigDecimal weeklyRent,
                @NotNull @DecimalMin("0.01") BigDecimal bondAmount,
                @NotNull PaymentFrequency paymentFrequency,
                String inspectionFrequency,
                String notes) {
}
