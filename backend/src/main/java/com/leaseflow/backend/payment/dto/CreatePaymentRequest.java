package com.leaseflow.backend.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.leaseflow.backend.payment.entity.PaymentMethod;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotNull LocalDate dueDate,
        @NotNull PaymentMethod paymentMethod,
        String reference,
        String notes) {
}
