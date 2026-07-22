package com.leaseflow.backend.common.exception.lease;

public class InvalidLeaseDateException extends RuntimeException{

    public InvalidLeaseDateException(){
        super("Lease end date must be after lease start date");
    }

}
