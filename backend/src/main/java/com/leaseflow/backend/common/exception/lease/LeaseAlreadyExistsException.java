package com.leaseflow.backend.common.exception.lease;

public class LeaseAlreadyExistsException extends RuntimeException{

    public LeaseAlreadyExistsException(Long propertyId){
        super("This property has been leased"); 
    }
}
