package com.leaseflow.backend.lease.mapper;

import org.springframework.stereotype.Component;

import com.leaseflow.backend.lease.dto.CreateLeaseRequest;
import com.leaseflow.backend.lease.dto.LeaseResponse;
import com.leaseflow.backend.lease.dto.UpdateLeaseRequest;
import com.leaseflow.backend.lease.entity.Lease;

@Component
public class LeaseMapper {

    public Lease toEntity(CreateLeaseRequest request) {
        Lease lease = new Lease();

        lease.setLeaseStart(request.leaseStart());
        lease.setLeaseEnd(request.leaseEnd());
        lease.setWeeklyRent(request.weeklyRent());
        lease.setBondAmount(request.bondAmount());
        lease.setPaymentFrequency(request.paymentFrequency());
        lease.setInspectionFrequency(request.inspectionFrequency());
        lease.setNotes(request.notes());

        return lease;
    }

    public LeaseResponse toResponse(Lease lease) {
        return new LeaseResponse(
                lease.getId(),
                lease.getLeaseStart(),
                lease.getLeaseEnd(),
                lease.getWeeklyRent(),
                lease.getBondAmount(),
                lease.getPaymentFrequency(),
                lease.getInspectionFrequency(),
                lease.getNotes());
    }

    public void updateEntity(Lease lease, UpdateLeaseRequest request) {
        if (request.leaseStart() != null)
            lease.setLeaseStart(request.leaseStart());
        if (request.leaseEnd() != null)
            lease.setLeaseEnd(request.leaseEnd());
        if (request.weeklyRent() != null)
            lease.setWeeklyRent(request.weeklyRent());
        if (request.bondAmount() != null)
            lease.setBondAmount(request.bondAmount());
        if (request.paymentFrequency() != null)
            lease.setPaymentFrequency(request.paymentFrequency());
        if (request.inspectionFrequency() != null)
            lease.setInspectionFrequency(request.inspectionFrequency());
        if (request.notes() != null)
            lease.setNotes(request.notes());
    }

}
