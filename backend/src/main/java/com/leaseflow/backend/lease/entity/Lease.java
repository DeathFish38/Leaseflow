package com.leaseflow.backend.lease.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.leaseflow.backend.common.entity.BaseEntity;
import com.leaseflow.backend.property.entity.Property;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "leases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lease extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lease_start", nullable = false)
    private LocalDate leaseStart;

    @Column(name = "lease_end", nullable = false)
    private LocalDate leaseEnd;

    @Column(name = "weekly_rent", nullable = false)
    private BigDecimal weeklyRent;

    @Column(name = "bond_amount", nullable = false)
    private BigDecimal bondAmount;

    @Column(name = "inspection_frequency")
    private String inspectionFrequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_frequency", nullable = false)
    private PaymentFrequency paymentFrequency;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;

}
