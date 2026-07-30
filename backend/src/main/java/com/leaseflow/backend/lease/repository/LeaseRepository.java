package com.leaseflow.backend.lease.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leaseflow.backend.lease.entity.Lease;

public interface LeaseRepository extends JpaRepository<Lease, Long> {
    Optional<Lease> findByPropertyId(Long propertyId);

    List<Lease> findPropertyByOwnerId(Long userId);

    // active lease
    // property.owner.id = userId AND leaseStart <= today AND leaseEnd >= today
    long countByPropertyOwnerIdAndLeaseStartLessThanEqualAndLeaseEndGreaterThanEqual(Long userId,
            LocalDate todayForStart, LocalDate todayForEnd);

}