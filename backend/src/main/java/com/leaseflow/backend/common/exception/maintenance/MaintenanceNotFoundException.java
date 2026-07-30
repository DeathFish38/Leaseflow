package com.leaseflow.backend.common.exception.maintenance;

public class MaintenanceNotFoundException extends RuntimeException{
        public MaintenanceNotFoundException(Long maintenanceId) {
        super("Maintenance request not found: " + maintenanceId);
    }

}
