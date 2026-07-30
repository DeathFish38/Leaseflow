package com.leaseflow.backend.maintenance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leaseflow.backend.maintenance.entity.MaintenanceRequest;
import com.leaseflow.backend.maintenance.entity.MaintenanceStatus;

public interface MaintenanceRepository extends JpaRepository<MaintenanceRequest, Long> {
    List<MaintenanceRequest> findByPropertyId(Long propertyId);

    List<MaintenanceRequest> findByPropertyIdAndStatus(Long propertyId, MaintenanceStatus status);
}
