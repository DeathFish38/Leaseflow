package com.leaseflow.backend.lease.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leaseflow.backend.lease.dto.CreateLeaseRequest;
import com.leaseflow.backend.lease.dto.LeaseResponse;
import com.leaseflow.backend.lease.dto.UpdateLeaseRequest;
import com.leaseflow.backend.lease.service.LeaseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LeaseController {

    private final LeaseService leaseService;

    @PostMapping("/properties/{propertyId}/lease")
    public ResponseEntity<LeaseResponse> createLease(@PathVariable Long propertyId,
            @Valid @RequestBody CreateLeaseRequest request) {
        LeaseResponse response = leaseService.createLease(propertyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // 201 Created

    @GetMapping("/properties/{propertyId}/lease")
    public ResponseEntity<LeaseResponse> getLeaseByPropertyId(@PathVariable Long propertyId) {
        return ResponseEntity.ok(leaseService.getLeaseByPropertyId(propertyId));
    }
    // 200 Ok 

    @PatchMapping("/lease/{leaseId}")
    public ResponseEntity<LeaseResponse> updateLease(@PathVariable Long leaseId, @RequestBody UpdateLeaseRequest request) {
        return ResponseEntity.ok(leaseService.updateLease(leaseId, request));
    }
    //200 Ok

    @DeleteMapping("/lease/{leaseId}")
    public ResponseEntity<Void> deleteLease(@PathVariable Long leaseId){
        leaseService.deleteLease(leaseId);
        return ResponseEntity.noContent().build();
    }
    // 204 no content 

}
