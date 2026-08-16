package com.leaseflow.backend.lease.service;

import org.springframework.stereotype.Service;

import com.leaseflow.backend.auth.service.AuthService;
import com.leaseflow.backend.common.exception.lease.InvalidLeaseDateException;
import com.leaseflow.backend.common.exception.lease.LeaseAlreadyExistsException;
import com.leaseflow.backend.common.exception.lease.LeaseNotFoundException;
import com.leaseflow.backend.common.exception.property.PropertyNotFoundException;
import com.leaseflow.backend.lease.dto.CreateLeaseRequest;
import com.leaseflow.backend.lease.dto.LeaseResponse;
import com.leaseflow.backend.lease.dto.UpdateLeaseRequest;
import com.leaseflow.backend.lease.entity.Lease;
import com.leaseflow.backend.lease.mapper.LeaseMapper;
import com.leaseflow.backend.lease.repository.LeaseRepository;
import com.leaseflow.backend.payment.service.PaymentService;
import com.leaseflow.backend.property.entity.Property;
import com.leaseflow.backend.property.repository.PropertyRepository;
import com.leaseflow.backend.users.entity.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaseService {

    private final LeaseRepository leaseRepository;
    private final PropertyRepository propertyRepository;
    private final LeaseMapper leaseMapper;
    // auto generate many payments for a lease
    private final PaymentService paymentService;
    private final AuthService authService;

    @Transactional
    public LeaseResponse createLease(Long propertyId, CreateLeaseRequest request) {
        Property property = getPropertyForAuthenticatedUser(propertyId);

        if (leaseRepository.findByPropertyId(propertyId).isPresent()) {
            throw new LeaseAlreadyExistsException(propertyId);
        }

        // validate correct leasing range
        validateDates(request.leaseStart(), request.leaseEnd());
        // convert to lease scheme for entity to save
        Lease lease = leaseMapper.toEntity(request);
        lease.setProperty(property);
        // save lease
        Lease savedLease = leaseRepository.save(lease);

        // auto generate all payemnts for a lease
        paymentService.generatePayments(savedLease);

        return leaseMapper.toResponse(savedLease);
    }

    // get all lease by property might have different lease term
    public LeaseResponse getLeaseByPropertyId(Long propertyId) {
        User user = authService.getAuthenticatedUser();
        Lease lease = leaseRepository.findByPropertyId(propertyId)
                .orElseThrow(() -> new LeaseNotFoundException(propertyId));
        if (!lease.getProperty().getOwner().getId().equals(user.getId())) {
            throw new LeaseNotFoundException(lease.getId());
        }
        return leaseMapper.toResponse(lease);
    }

    // update lease
    public LeaseResponse updateLease(Long leaseId, UpdateLeaseRequest request) {
        Lease lease = getLeaseForAuthenticatedUser(leaseId);

        if (request.leaseStart() != null && request.leaseEnd() != null) {
            validateDates(request.leaseStart(), request.leaseEnd());
        }

        leaseMapper.updateEntity(lease, request);
        Lease updatedLease = leaseRepository.save(lease);
        return leaseMapper.toResponse(updatedLease);
    }

    // delete lease
    public void deleteLease(Long leaseId) {
        Lease lease = getLeaseForAuthenticatedUser(leaseId);
        leaseRepository.delete(lease);
    }

    // helper methods
    // return lease object
    private Lease getLeaseForAuthenticatedUser(Long leaseId) {
        User user = authService.getAuthenticatedUser();
        Lease lease = leaseRepository.findById(leaseId)
                .orElseThrow(() -> new LeaseNotFoundException(leaseId));
        if (!lease.getProperty().getOwner().getId().equals(user.getId())) {
            throw new LeaseNotFoundException(leaseId);
        }
        return lease;
    }

    // return property object
    private Property getPropertyForAuthenticatedUser(Long propertyId) {
        User user = authService.getAuthenticatedUser();
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));

        if (!property.getOwner().getId().equals(user.getId())) {
            throw new PropertyNotFoundException(propertyId);
        }
        return property;
    }

    // validate date
    private void validateDates(java.time.LocalDate start, java.time.LocalDate end) {
        if (end.isBefore(start) || end.isEqual(start)) {
            throw new InvalidLeaseDateException();
        }
    }

}
