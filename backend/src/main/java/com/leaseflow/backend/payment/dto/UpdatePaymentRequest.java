package com.leaseflow.backend.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.leaseflow.backend.payment.entity.PaymentMethod;

public record UpdatePaymentRequest(
        BigDecimal amount,
        LocalDate dueDate,
        PaymentMethod paymentMethod,
        String reference,
        String notes) {

}
