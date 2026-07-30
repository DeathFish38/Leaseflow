package com.leaseflow.backend.maintenance.dto;

import com.leaseflow.backend.maintenance.entity.MaintenancePriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMaintenanceRequest(
    @NotBlank String title, 
    @NotBlank String description, 
    @NotNull MaintenancePriority priority
) {}

// client gives -> title, description, priority
// backlend decides -> status = OPEN, reportedDate = today 


