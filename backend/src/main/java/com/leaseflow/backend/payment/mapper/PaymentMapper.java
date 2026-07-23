package com.leaseflow.backend.payment.mapper;

import org.springframework.stereotype.Component;

import com.leaseflow.backend.payment.dto.CreatePaymentRequest;
import com.leaseflow.backend.payment.dto.PaymentResponse;
import com.leaseflow.backend.payment.dto.UpdatePaymentRequest;
import com.leaseflow.backend.payment.entity.Payment;

@Component
public class PaymentMapper {

    public Payment toEntity(CreatePaymentRequest request) {

        Payment payment = new Payment();

        payment.setAmount(request.amount());
        payment.setDueDate(request.dueDate());
        payment.setPaymentMethod(request.paymentMethod());
        payment.setReference(request.reference());
        payment.setNotes(request.notes());

        return payment;
    }

    public void updateEntity(
            Payment payment,
            UpdatePaymentRequest request) {

        if (request.amount() != null) {
            payment.setAmount(request.amount());
        }

        if (request.dueDate() != null) {
            payment.setDueDate(request.dueDate());
        }

        if (request.paymentMethod() != null) {
            payment.setPaymentMethod(request.paymentMethod());
        }

        if (request.reference() != null) {
            payment.setReference(request.reference());
        }

        if (request.notes() != null) {
            payment.setNotes(request.notes());
        }
    }

    public PaymentResponse toResponse(Payment payment) {

        return new PaymentResponse(
                payment.getId(),
                payment.getLease().getId(),
                payment.getAmount(),
                payment.getDueDate(),
                payment.getPaidDate(),
                payment.getStatus(),
                payment.getPaymentMethod(),
                payment.getReference(),
                payment.getNotes());
    }
}