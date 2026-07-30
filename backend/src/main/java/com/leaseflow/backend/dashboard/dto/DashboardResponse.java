package com.leaseflow.backend.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.leaseflow.backend.payment.entity.PaymentStatus;

// dashboard service for aggregation
public record DashboardResponse(
        long propertyCount,
        long activeLeaseCount,
        BigDecimal weeklyRent,
        NextPaymentResponse nextPayment,
        BigDecimal outstandingRent,
        long overduePayments,
        long openMaintenanceRequests) {
    public record NextPaymentResponse(
            BigDecimal amount,
            LocalDate dueDate,
            PaymentStatus status) {
    }
}
