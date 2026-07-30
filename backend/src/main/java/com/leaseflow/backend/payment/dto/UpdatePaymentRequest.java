package com.leaseflow.backend.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.leaseflow.backend.payment.entity.PaymentMethod;

import jakarta.validation.constraints.DecimalMin;

public record UpdatePaymentRequest(
        @DecimalMin("0.01") BigDecimal amount,
        LocalDate dueDate,
        PaymentMethod paymentMethod,
        String reference,
        String notes) {

}
