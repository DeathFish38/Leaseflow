package com.leaseflow.backend.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leaseflow.backend.payment.entity.Payment;


public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // find all payments based on its lease
    List<Payment> findByLeaseId(Long leaseId);
}
