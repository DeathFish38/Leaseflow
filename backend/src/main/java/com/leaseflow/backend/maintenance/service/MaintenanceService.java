package com.leaseflow.backend.maintenance.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.leaseflow.backend.auth.service.AuthService;
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
import com.leaseflow.backend.users.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;
    private final PropertyRepository propertyRepository;
    private final MaintenanceMapper maintenanceMapper;
    private final AuthService authService;

    // create maintenance
    public MaintenanceResponse createMaintenance(Long propertyId, CreateMaintenanceRequest request) {
        Property property = getPropertyForAuthenticatedUser(propertyId);
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
        getPropertyForAuthenticatedUser(propertyId);
        return maintenanceRepository
                .findByPropertyId(propertyId)
                .stream()
                .map(maintenanceMapper::toResponse)
                .toList();
    }

    // get by a request id
    public MaintenanceResponse getMaintenanceById(Long maintenanceId) {
        return maintenanceMapper.toResponse(getMaintenanceForAuthenticatedUser(maintenanceId));
    }

    // update
    public MaintenanceResponse updateMaintenance(Long maintenanceId, UpdateMaintenanceRequest request) {
        MaintenanceRequest maintenance = getMaintenanceForAuthenticatedUser(maintenanceId);
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
        MaintenanceRequest maintenance = getMaintenanceForAuthenticatedUser(maintenanceId);
        maintenanceRepository.delete(maintenance);
    }

    // Workflow methods
    // start a request
    public MaintenanceResponse startMaintenance(Long maintenanceId) {
        MaintenanceRequest maintenance = getMaintenanceForAuthenticatedUser(maintenanceId);
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
        MaintenanceRequest maintenance = getMaintenanceForAuthenticatedUser(maintenanceId);

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
    private Property getPropertyForAuthenticatedUser(Long propertyId) {

        User user = authService.getAuthenticatedUser();

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));

        if (!property.getOwner().getId().equals(user.getId())) {
            throw new PropertyNotFoundException(propertyId);
        }

        return property;
    }

    // return a maintenanceRequest object
    private MaintenanceRequest getMaintenanceForAuthenticatedUser(Long maintenanceId) {
        User user = authService.getAuthenticatedUser();
        MaintenanceRequest maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new MaintenanceNotFoundException(maintenanceId));
        if (!maintenance.getProperty()
                .getOwner()
                .getId()
                .equals(user.getId())) {

            throw new MaintenanceNotFoundException(maintenanceId);
        }
        return maintenance;
    }

}
