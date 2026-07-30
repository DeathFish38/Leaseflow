package com.leaseflow.backend.payment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leaseflow.backend.payment.dto.CreatePaymentRequest;
import com.leaseflow.backend.payment.dto.PaymentResponse;
import com.leaseflow.backend.payment.dto.UpdatePaymentRequest;
import com.leaseflow.backend.payment.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // create new payment
    @PostMapping("/leases/{leaseId}/payments")
    public ResponseEntity<PaymentResponse> createPayment(
            @PathVariable Long leaseId,
            @Valid @RequestBody CreatePaymentRequest request) {

        PaymentResponse response = paymentService.createPayment(leaseId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // get all payments for a lease
    @GetMapping("/leases/{leaseId}/payments")
    public ResponseEntity<List<PaymentResponse>> getPayments(
            @PathVariable Long leaseId) {

        List<PaymentResponse> response = paymentService.getPayments(leaseId);

        return ResponseEntity.ok(response);
    }

    // get payment by id
    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(
            @PathVariable Long paymentId) {

        PaymentResponse response = paymentService.getPaymentById(paymentId);

        return ResponseEntity.ok(response);
    }

    // update payment
    @PatchMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentResponse> updatePayment(
            @PathVariable Long paymentId,
            @Valid @RequestBody UpdatePaymentRequest request) {

        PaymentResponse response = paymentService.updatePayment(paymentId, request);

        return ResponseEntity.ok(response);
    }

    // mark payment as paid
    @PatchMapping("/payments/{paymentId}/mark-paid")
    public ResponseEntity<PaymentResponse> markAsPaid(
            @PathVariable Long paymentId) {

        PaymentResponse response = paymentService.markAsPaid(paymentId);

        return ResponseEntity.ok(response);
    }

    // delete payment
    @DeleteMapping("/payments/{paymentId}")
    public ResponseEntity<Void> deletePayment(
            @PathVariable Long paymentId) {

        paymentService.deletePayment(paymentId);

        return ResponseEntity.noContent().build();
    }

    // add business oriented endpoints - add later
    // GET /api/leases/{leaseId}/payments/next – returns the next upcoming payment.
    // GET /api/leases/{leaseId}/payments/overdue – returns overdue payments.
    // GET /api/dashboard/payments/summary – returns totals such as amount paid,
    // outstanding balance, and next due payment.

}
