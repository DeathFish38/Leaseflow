package com.leaseflow.backend.payment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @PostMapping("/leases/{leaseId}/payments")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse createPayment(
            @PathVariable Long leaseId,
            @Valid @RequestBody CreatePaymentRequest request) {

        return paymentService.createPayment(leaseId, request);
    }

    @GetMapping("/leases/{leaseId}/payments")
    public List<PaymentResponse> getPayments(
            @PathVariable Long leaseId) {

        return paymentService.getPayments(leaseId);
    }

    @GetMapping("/payments/{paymentId}")
    public PaymentResponse getPaymentById(
            @PathVariable Long paymentId) {

        return paymentService.getPaymentById(paymentId);
    }

    @PatchMapping("/payments/{paymentId}")
    public PaymentResponse updatePayment(
            @PathVariable Long paymentId,
            @RequestBody UpdatePaymentRequest request) {

        return paymentService.updatePayment(paymentId, request);
    }

    @PatchMapping("/payments/{paymentId}/mark-paid")
    public PaymentResponse markAsPaid(
            @PathVariable Long paymentId) {

        return paymentService.markAsPaid(paymentId);
    }

    @DeleteMapping("/payments/{paymentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePayment(
            @PathVariable Long paymentId) {

        paymentService.deletePayment(paymentId);
    }

    // add business oriented endpoints
    // GET /api/leases/{leaseId}/payments/next – returns the next upcoming payment.
    // GET /api/leases/{leaseId}/payments/overdue – returns overdue payments.
    // GET /api/dashboard/payments/summary – returns totals such as amount paid, outstanding balance, and next due payment.
}