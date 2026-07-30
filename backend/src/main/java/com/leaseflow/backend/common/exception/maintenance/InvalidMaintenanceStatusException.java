package com.leaseflow.backend.common.exception.maintenance;

public class InvalidMaintenanceStatusException extends RuntimeException {
    public InvalidMaintenanceStatusException(String message) {
        super(message);
    }
}
