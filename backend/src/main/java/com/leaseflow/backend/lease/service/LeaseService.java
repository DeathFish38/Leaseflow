package com.leaseflow.backend.lease.service;

import org.springframework.stereotype.Service;

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
import com.leaseflow.backend.property.entity.Property;
import com.leaseflow.backend.property.repository.PropertyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaseService {

    private final LeaseRepository leaseRepository;
    private final PropertyRepository propertyRepository;
    private final LeaseMapper leaseMapper;

    public LeaseResponse createLease(Long propertyId, CreateLeaseRequest request) {
        Property property = getProperty(propertyId);

        if (leaseRepository.findByPropertyId(propertyId).isPresent()) {
            throw new LeaseAlreadyExistsException(propertyId);
        }

        // validate correct leasing range
        validateDates(request.leaseStart(), request.leaseEnd());

        Lease lease = leaseMapper.toEntity(request);
        lease.setProperty(property);

        Lease savedLease = leaseRepository.save(lease);
        return leaseMapper.toResponse(savedLease);
    }

    // get all lease by property might have different lease term
    public LeaseResponse getLeaseByPropertyId(Long propertyId) {
        Lease lease = leaseRepository.findByPropertyId(propertyId)
                .orElseThrow(() -> new LeaseNotFoundException(propertyId));
        return leaseMapper.toResponse(lease);
    }

    // update lease
    public LeaseResponse updateLease(Long leaseId, UpdateLeaseRequest request) {
        Lease lease = getLease(leaseId);

        if (request.leaseStart() != null && request.leaseEnd() != null) {
            validateDates(request.leaseStart(), request.leaseEnd());
        }

        leaseMapper.updateEntity(lease, request);
        Lease updatedLease = leaseRepository.save(lease);
        return leaseMapper.toResponse(updatedLease);
    }

    // delete lease
    public void deleteLease(Long leaseId) {
        Lease lease = getLease(leaseId);
        leaseRepository.delete(lease);
    }

    // helper methods
    // return lease object
    private Lease getLease(Long leaseId) {
        return leaseRepository.findById(leaseId).orElseThrow(() -> new LeaseNotFoundException(leaseId));

    }

    // return property object
    private Property getProperty(Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
    }

    // validate date
    private void validateDates(java.time.LocalDate start, java.time.LocalDate end) {
        if (end.isBefore(start) || end.isEqual(start)) {
            throw new InvalidLeaseDateException();
        }
    }

}
