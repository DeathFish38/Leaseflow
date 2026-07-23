package com.leaseflow.backend.common.exception.payment;

public class PaymentAlreadyPaidException extends RuntimeException {

    public PaymentAlreadyPaidException(Long paymentId) {
        super("Payment has already been marked as paid. Payment id: " + paymentId);
    }

}
