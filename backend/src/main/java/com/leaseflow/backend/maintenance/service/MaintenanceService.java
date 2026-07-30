package com.leaseflow.backend.maintenance.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.leaseflow.backend.common.exception.maintenance.InvalidMaintenanceStatusException;
import com.leaseflow.backend.common.exception.maintenance.MaintenanceNotFoundException;
import com.leaseflow.backend.common.exception.property.PropertyNotFoundException;
import com.leaseflow.backend.maintenance.dto.CreateMaintenanceRequest;
import com.leaseflow.backend.maintenance.dto.MaintenanceResponse;
import com.leaseflow.backend.maintenance.dto.UpdateMaintenanceRequest;
import com.leaseflow.backend.maintenance.entity.MaintenanceRequest;
import com.leaseflow.backend.maintenance.entity.MaintenanceStatus;
import com.leaseflow.backend.maintenance.mapper.MaintenanceMapper;
import com.leaseflow.backend.maintenance.repository.MaintenanceRepository;
import com.leaseflow.backend.property.entity.Property;
import com.leaseflow.backend.property.repository.PropertyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;
    private final PropertyRepository propertyRepository;
    private final MaintenanceMapper maintenanceMapper;

    // create maintenance
    public MaintenanceResponse createMaintenance(Long propertyId, CreateMaintenanceRequest request) {
        Property property = getProperty(propertyId);
        // convert to entity schema
        MaintenanceRequest maintenance = maintenanceMapper.toEntity(request);
        maintenance.setProperty(property);
        maintenance.setStatus(MaintenanceStatus.OPEN);
        maintenance.setReportedDate(LocalDate.now());
        // save to repo
        MaintenanceRequest saved = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toResponse(saved);
    }

    // get all maintenances
    public List<MaintenanceResponse> getMaintenanceByProperty(Long propertyId) {
        getProperty(propertyId);
        return maintenanceRepository
                .findByPropertyId(propertyId)
                .stream()
                .map(maintenanceMapper::toResponse)
                .toList();
    }

    // get by a request id
    public MaintenanceResponse getMaintenanceById(Long maintenanceId) {
        return maintenanceMapper.toResponse(getMaintenance(maintenanceId));
    }

    // update
    public MaintenanceResponse updateMaintenance(Long maintenanceId, UpdateMaintenanceRequest request) {
        MaintenanceRequest maintenance = getMaintenance(maintenanceId);
        if (maintenance.getStatus() == MaintenanceStatus.COMPLETED) {
            throw new InvalidMaintenanceStatusException(
                    "Completed maintenance requests cannot be updated.");
        }

        // convert
        maintenanceMapper.updateEntity(
                maintenance,
                request);

        MaintenanceRequest updated = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toResponse(updated);
    }

    // delete
    public void deleteMaintenance(Long maintenanceId) {
        MaintenanceRequest maintenance = getMaintenance(maintenanceId);
        maintenanceRepository.delete(maintenance);
    }

    // Workflow methods
    // start a request
    public MaintenanceResponse startMaintenance(Long maintenanceId) {
        MaintenanceRequest maintenance = getMaintenance(maintenanceId);
        if (maintenance.getStatus() != MaintenanceStatus.OPEN) {

            throw new InvalidMaintenanceStatusException(
                    "Only OPEN maintenance requests can be started.");
        }
        maintenance.setStatus(MaintenanceStatus.IN_PROGRESS);

        MaintenanceRequest updated = maintenanceRepository.save(maintenance);

        return maintenanceMapper.toResponse(updated);
    }

    // complete a request
    public MaintenanceResponse completeMaintenance(Long maintenanceId) {
        MaintenanceRequest maintenance = getMaintenance(maintenanceId);

        if (maintenance.getStatus() != MaintenanceStatus.IN_PROGRESS) {

            throw new InvalidMaintenanceStatusException(
                    "Only IN_PROGRESS maintenance requests can be completed.");
        }
        maintenance.setStatus(MaintenanceStatus.COMPLETED);
        maintenance.setResolvedDate(LocalDate.now());
        MaintenanceRequest updated = maintenanceRepository.save(maintenance);

        return maintenanceMapper.toResponse(updated);
    }

    //

    // Helper methods
    // return property object
    private Property getProperty(Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
    }

    // return a maintenanceRequest object
    private MaintenanceRequest getMaintenance(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new MaintenanceNotFoundException(id));
    }

}
