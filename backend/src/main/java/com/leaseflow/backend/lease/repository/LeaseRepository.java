package com.leaseflow.backend.lease.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leaseflow.backend.lease.entity.Lease;

public interface LeaseRepository extends JpaRepository<Lease, Long> {
    Optional<Lease> findByPropertyId(Long propertyId);
}