package com.leaseflow.backend.maintenance.dto;

import java.time.LocalDate;

import com.leaseflow.backend.maintenance.entity.MaintenancePriority;
import com.leaseflow.backend.maintenance.entity.MaintenanceStatus;

public record MaintenanceResponse(
        Long id,
        Long propertyId,
        String title,
        String description,
        MaintenancePriority priority,
        MaintenanceStatus status,
        LocalDate reportedDate,
        LocalDate resolvedDate) {
}
