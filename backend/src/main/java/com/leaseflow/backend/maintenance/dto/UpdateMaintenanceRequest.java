package com.leaseflow.backend.maintenance.dto;

import com.leaseflow.backend.maintenance.entity.MaintenancePriority;

public record UpdateMaintenanceRequest(
    String title,
    String description,
    MaintenancePriority priority
) {}