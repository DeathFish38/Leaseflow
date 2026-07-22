package com.leaseflow.backend.lease.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.leaseflow.backend.lease.entity.PaymentFrequency;

public record UpdateLeaseRequest(
        LocalDate leaseStart,
        LocalDate leaseEnd,
        BigDecimal weeklyRent,
        BigDecimal bondAmount,
        PaymentFrequency paymentFrequency,
        String inspectionFrequency,
        String notes) {

}
