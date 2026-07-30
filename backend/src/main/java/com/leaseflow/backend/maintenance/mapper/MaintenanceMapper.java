package com.leaseflow.backend.maintenance.mapper;

import org.springframework.stereotype.Component;

import com.leaseflow.backend.maintenance.dto.CreateMaintenanceRequest;
import com.leaseflow.backend.maintenance.dto.MaintenanceResponse;
import com.leaseflow.backend.maintenance.dto.UpdateMaintenanceRequest;
import com.leaseflow.backend.maintenance.entity.MaintenanceRequest;

@Component
public class MaintenanceMapper {

    public MaintenanceRequest toEntity(CreateMaintenanceRequest request) {

        MaintenanceRequest maintenance = new MaintenanceRequest();

        maintenance.setTitle(request.title());
        maintenance.setDescription(request.description());
        maintenance.setPriority(request.priority());

        return maintenance;
    }

    public void updateEntity(MaintenanceRequest maintenance, UpdateMaintenanceRequest request) {

        if (request.title() != null) {
            maintenance.setTitle(request.title());
        }

        if (request.description() != null) {
            maintenance.setDescription(request.description());
        }

        if (request.priority() != null) {
            maintenance.setPriority(request.priority());
        }
    }

    public MaintenanceResponse toResponse(MaintenanceRequest maintenance) {
        return new MaintenanceResponse(
                maintenance.getId(),
                maintenance.getProperty().getId(),
                maintenance.getTitle(),
                maintenance.getDescription(),
                maintenance.getPriority(),
                maintenance.getStatus(),
                maintenance.getReportedDate(),
                maintenance.getResolvedDate());
    }

}
