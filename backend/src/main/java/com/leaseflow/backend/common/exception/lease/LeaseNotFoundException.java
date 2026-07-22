package com.leaseflow.backend.common.exception.lease;

public class LeaseNotFoundException extends RuntimeException{
    public LeaseNotFoundException(Long leaseId){
        super("Lease not found with id: " + leaseId); 
    }

}
