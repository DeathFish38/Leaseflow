package com.leaseflow.backend.maintenance.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class MaintenanceServiceTest {

    @Mock
    private MaintenanceRepository maintenanceRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private MaintenanceMapper maintenanceMapper;

    @InjectMocks
    private MaintenanceService maintenanceService;

    private Property property;
    private MaintenanceRequest maintenance;

    @BeforeEach
    void setUp() {

        property = new Property();
        property.setId(1L);

        maintenance = new MaintenanceRequest();
        maintenance.setId(1L);
        maintenance.setProperty(property);
        maintenance.setStatus(MaintenanceStatus.OPEN);
    }

    @Test
    void createMaintenance_shouldCreateOpenRequest() {

        CreateMaintenanceRequest request =
                mock(CreateMaintenanceRequest.class);

        when(propertyRepository.findById(1L))
                .thenReturn(Optional.of(property));

        when(maintenanceMapper.toEntity(request))
                .thenReturn(maintenance);

        when(maintenanceRepository.save(maintenance))
                .thenReturn(maintenance);

        MaintenanceResponse response =
                mock(MaintenanceResponse.class);

        when(maintenanceMapper.toResponse(maintenance))
                .thenReturn(response);

        MaintenanceResponse result =
                maintenanceService.createMaintenance(1L, request);

        assertSame(response, result);

        assertEquals(
                MaintenanceStatus.OPEN,
                maintenance.getStatus()
        );

        assertNotNull(
                maintenance.getReportedDate()
        );

        verify(maintenanceRepository)
                .save(maintenance);
    }

    @Test
    void createMaintenance_shouldThrowWhenPropertyNotFound() {

        CreateMaintenanceRequest request =
                mock(CreateMaintenanceRequest.class);

        when(propertyRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                PropertyNotFoundException.class,
                () -> maintenanceService
                        .createMaintenance(1L, request)
        );

        verify(maintenanceRepository, never())
                .save(any());
    }

    @Test
    void getMaintenanceByProperty_shouldReturnRequests() {

        when(propertyRepository.findById(1L))
                .thenReturn(Optional.of(property));

        when(maintenanceRepository.findByPropertyId(1L))
                .thenReturn(List.of(maintenance));

        MaintenanceResponse response =
                mock(MaintenanceResponse.class);

        when(maintenanceMapper.toResponse(maintenance))
                .thenReturn(response);

        List<MaintenanceResponse> result =
                maintenanceService
                        .getMaintenanceByProperty(1L);

        assertEquals(1, result.size());
        assertSame(response, result.get(0));
    }

    @Test
    void getMaintenanceById_shouldReturnRequest() {

        when(maintenanceRepository.findById(1L))
                .thenReturn(Optional.of(maintenance));

        MaintenanceResponse response =
                mock(MaintenanceResponse.class);

        when(maintenanceMapper.toResponse(maintenance))
                .thenReturn(response);

        MaintenanceResponse result =
                maintenanceService.getMaintenanceById(1L);

        assertSame(response, result);
    }

    @Test
    void getMaintenanceById_shouldThrowWhenNotFound() {

        when(maintenanceRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                MaintenanceNotFoundException.class,
                () -> maintenanceService
                        .getMaintenanceById(1L)
        );
    }

    @Test
    void startMaintenance_shouldChangeOpenToInProgress() {

        when(maintenanceRepository.findById(1L))
                .thenReturn(Optional.of(maintenance));

        when(maintenanceRepository.save(maintenance))
                .thenReturn(maintenance);

        MaintenanceResponse response =
                mock(MaintenanceResponse.class);

        when(maintenanceMapper.toResponse(maintenance))
                .thenReturn(response);

        MaintenanceResponse result =
                maintenanceService.startMaintenance(1L);

        assertSame(response, result);

        assertEquals(
                MaintenanceStatus.IN_PROGRESS,
                maintenance.getStatus()
        );

        verify(maintenanceRepository)
                .save(maintenance);
    }

    @Test
    void startMaintenance_shouldRejectNonOpenRequest() {

        maintenance.setStatus(
                MaintenanceStatus.IN_PROGRESS
        );

        when(maintenanceRepository.findById(1L))
                .thenReturn(Optional.of(maintenance));

        assertThrows(
                InvalidMaintenanceStatusException.class,
                () -> maintenanceService
                        .startMaintenance(1L)
        );

        verify(maintenanceRepository, never())
                .save(any());
    }

    @Test
    void completeMaintenance_shouldCompleteRequest() {

        maintenance.setStatus(
                MaintenanceStatus.IN_PROGRESS
        );

        when(maintenanceRepository.findById(1L))
                .thenReturn(Optional.of(maintenance));

        when(maintenanceRepository.save(maintenance))
                .thenReturn(maintenance);

        MaintenanceResponse response =
                mock(MaintenanceResponse.class);

        when(maintenanceMapper.toResponse(maintenance))
                .thenReturn(response);

        MaintenanceResponse result =
                maintenanceService.completeMaintenance(1L);

        assertSame(response, result);

        assertEquals(
                MaintenanceStatus.COMPLETED,
                maintenance.getStatus()
        );

        assertNotNull(
                maintenance.getResolvedDate()
        );

        verify(maintenanceRepository)
                .save(maintenance);
    }

    @Test
    void completeMaintenance_shouldRejectOpenRequest() {

        maintenance.setStatus(
                MaintenanceStatus.OPEN
        );

        when(maintenanceRepository.findById(1L))
                .thenReturn(Optional.of(maintenance));

        assertThrows(
                InvalidMaintenanceStatusException.class,
                () -> maintenanceService
                        .completeMaintenance(1L)
        );

        verify(maintenanceRepository, never())
                .save(any());
    }

    @Test
    void updateMaintenance_shouldRejectCompletedRequest() {

        maintenance.setStatus(
                MaintenanceStatus.COMPLETED
        );

        when(maintenanceRepository.findById(1L))
                .thenReturn(Optional.of(maintenance));

        UpdateMaintenanceRequest request =
                mock(UpdateMaintenanceRequest.class);

        assertThrows(
                InvalidMaintenanceStatusException.class,
                () -> maintenanceService
                        .updateMaintenance(1L, request)
        );

        verify(maintenanceRepository, never())
                .save(any());
    }

    @Test
    void deleteMaintenance_shouldDeleteRequest() {

        when(maintenanceRepository.findById(1L))
                .thenReturn(Optional.of(maintenance));

        maintenanceService.deleteMaintenance(1L);

        verify(maintenanceRepository)
                .delete(maintenance);
    }
}