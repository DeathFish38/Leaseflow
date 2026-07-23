package com.leaseflow.backend.payment.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.leaseflow.backend.common.exception.lease.LeaseNotFoundException;
import com.leaseflow.backend.common.exception.payment.InvalidPaymentException;
import com.leaseflow.backend.common.exception.payment.PaymentAlreadyPaidException;
import com.leaseflow.backend.common.exception.payment.PaymentNotFoundException;
import com.leaseflow.backend.lease.entity.Lease;
import com.leaseflow.backend.lease.repository.LeaseRepository;
import com.leaseflow.backend.payment.dto.CreatePaymentRequest;
import com.leaseflow.backend.payment.dto.PaymentResponse;
import com.leaseflow.backend.payment.dto.UpdatePaymentRequest;
import com.leaseflow.backend.payment.entity.Payment;
import com.leaseflow.backend.payment.entity.PaymentStatus;
import com.leaseflow.backend.payment.mapper.PaymentMapper;
import com.leaseflow.backend.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final LeaseRepository leaseRepository;
    private final PaymentMapper paymentMapper;

    // create new payment
    public PaymentResponse createPayment(Long leaseId, CreatePaymentRequest request) {
        Lease lease = getLease(leaseId);
        // validate the payment date
        if (request.dueDate().isBefore(lease.getLeaseStart()) || request.dueDate().isAfter(lease.getLeaseEnd())) {
            throw new InvalidPaymentException("Payment due date must be within the lease period.");
        }

        // convert payment to entity schema for saving
        Payment payment = paymentMapper.toEntity(request);

        payment.setLease(lease);

        // check the status
        updatePaymentStatus(payment);

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toResponse(savedPayment);
    }

    // get payments
    public List<PaymentResponse> getPayments(Long leaseId) {

        getLease(leaseId);

        return paymentRepository.findByLeaseId(leaseId)
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    // get a payment by its id
    public PaymentResponse getPaymentById(Long paymentId) {

        return paymentMapper.toResponse(
                getPayment(paymentId));
    }

    // update payment
    public PaymentResponse updatePayment(Long paymentId,
            UpdatePaymentRequest request) {

        Payment payment = getPayment(paymentId);

        paymentMapper.updateEntity(payment, request);

        // check status
        updatePaymentStatus(payment);

        Payment updatedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponse(updatedPayment);
    }

    public PaymentResponse markAsPaid(Long paymentId) {

        Payment payment = getPayment(paymentId);

        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new PaymentAlreadyPaidException(paymentId);
        }

        payment.setPaidDate(LocalDate.now());
        payment.setStatus(PaymentStatus.PAID);

        Payment updatedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponse(updatedPayment);
    }

    public void deletePayment(Long paymentId) {

        paymentRepository.delete(getPayment(paymentId));
    }

    // helper methods
    // return lease object
    private Lease getLease(Long leaseId) {
        return leaseRepository.findById(leaseId).orElseThrow(() -> new LeaseNotFoundException(leaseId));
    }

    // return payment object
    private Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    // recalculate payment status
    private void updatePaymentStatus(Payment payment) {

        if (payment.getPaidDate() != null) {
            payment.setStatus(PaymentStatus.PAID);
        } else if (payment.getDueDate().isBefore(LocalDate.now())) {
            payment.setStatus(PaymentStatus.OVERDUE);
        } else {
            payment.setStatus(PaymentStatus.PENDING);
        }
    }

}
