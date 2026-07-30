package com.leaseflow.backend.payment.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.leaseflow.backend.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // find all payments based on its lease
    List<Payment> findByLeaseId(Long leaseId);

    // for dashboard
    // find next upcoming payment 
    @Query("""
        SELECT p 
        FROM Payment p 
        WHERE p.lease.property.owner.id = :userId 
        AND p.paidDate IS NULL 
        AND p.dueDate >= :today 
        ORDER BY p.dueDate ASC
    """)
    List<Payment> findNextPayments(Long userId, LocalDate today);

    // outstanding rent 
    @Query(""" 
        SELECT COALESCE(SUM(p.amount), 0) 
        FROM Payment p 
        WHERE p.lease.property.owner.id = :userId 
        AND p.paidDate IS NULL """) 
        BigDecimal calculateOutstandingRent( @Param("userId") Long userId);

    // overdue payment count
    @Query(""" 
        SELECT COUNT(p) 
        FROM Payment p 
        WHERE p.lease.property.owner.id = :userId 
        AND p.paidDate IS NULL 
        AND p.dueDate < :today """) 
        long countOverduePayments( @Param("userId") Long userId, @Param("today") LocalDate today);

}
