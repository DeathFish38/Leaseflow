package com.leaseflow.backend.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.leaseflow.backend.payment.entity.PaymentMethod;
import com.leaseflow.backend.payment.entity.PaymentStatus;

public record PaymentResponse(
                Long id,
                Long leaseId,
                BigDecimal amount,
                LocalDate dueDate,
                LocalDate paidDate,
                PaymentStatus status,
                PaymentMethod paymentMethod,
                String reference,
                String notes) {
}
