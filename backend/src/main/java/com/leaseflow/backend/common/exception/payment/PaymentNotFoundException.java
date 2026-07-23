package com.leaseflow.backend.common.exception.payment;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(Long paymentId) {
        super("Payment not found with id: " + paymentId);
    }
}
