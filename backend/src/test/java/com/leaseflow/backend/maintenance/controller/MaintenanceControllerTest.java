package com.leaseflow.backend.maintenance.controller;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leaseflow.backend.maintenance.dto.CreateMaintenanceRequest;
import com.leaseflow.backend.maintenance.dto.MaintenanceResponse;
import com.leaseflow.backend.maintenance.dto.UpdateMaintenanceRequest;
import com.leaseflow.backend.maintenance.entity.MaintenanceStatus;
import com.leaseflow.backend.maintenance.service.MaintenanceService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(MaintenanceController.class)
class MaintenanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MaintenanceService maintenanceService;

    @Test
    void createMaintenance_shouldReturnCreated() throws Exception {

        Long propertyId = 1L;

        CreateMaintenanceRequest request = new CreateMaintenanceRequest(
                "Leaking tap",
                "Kitchen tap is leaking", null);

        MaintenanceResponse response = new MaintenanceResponse(
                1L,
                propertyId,
                "Leaking tap",
                "Kitchen tap is leaking",
                null, MaintenanceStatus.OPEN,
                LocalDate.of(2026, 8, 1),
                null);

        when(maintenanceService.createMaintenance(
                eq(propertyId),
                any(CreateMaintenanceRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/properties/{propertyId}/maintenance", propertyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("OPEN"));

        verify(maintenanceService).createMaintenance(
                eq(propertyId),
                any(CreateMaintenanceRequest.class));
    }

    @Test
    void getMaintenanceByProperty_shouldReturnOk() throws Exception {

        Long propertyId = 1L;

        MaintenanceResponse response = new MaintenanceResponse(
                1L,
                propertyId,
                "Leaking tap",
                "Kitchen tap is leaking",
                null, MaintenanceStatus.OPEN,
                LocalDate.of(2026, 8, 1),
                null);

        when(maintenanceService.getMaintenanceByProperty(propertyId))
                .thenReturn(List.of(response));

        mockMvc.perform(
                get("/api/properties/{propertyId}/maintenance", propertyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("OPEN"));

        verify(maintenanceService)
                .getMaintenanceByProperty(propertyId);
    }

    @Test
    void getMaintenanceById_shouldReturnOk() throws Exception {

        Long maintenanceId = 1L;

        MaintenanceResponse response = new MaintenanceResponse(
                maintenanceId,
                1L,
                "Leaking tap",
                "Kitchen tap is leaking",
                null, MaintenanceStatus.OPEN,
                LocalDate.of(2026, 8, 1),
                null);

        when(maintenanceService.getMaintenanceById(maintenanceId))
                .thenReturn(response);

        mockMvc.perform(
                get("/api/maintenance/{maintenanceId}", maintenanceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("OPEN"));

        verify(maintenanceService)
                .getMaintenanceById(maintenanceId);
    }

    @Test
    void updateMaintenance_shouldReturnOk() throws Exception {

        Long maintenanceId = 1L;

        UpdateMaintenanceRequest request = new UpdateMaintenanceRequest(
                "Updated issue",
                "Updated description", null);

        MaintenanceResponse response = new MaintenanceResponse(
                maintenanceId,
                1L,
                "Updated issue",
                "Updated description",
                null, MaintenanceStatus.OPEN,
                LocalDate.of(2026, 8, 1),
                null);

        when(maintenanceService.updateMaintenance(
                eq(maintenanceId),
                any(UpdateMaintenanceRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                patch("/api/maintenance/{maintenanceId}", maintenanceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(maintenanceService).updateMaintenance(
                eq(maintenanceId),
                any(UpdateMaintenanceRequest.class));
    }

    @Test
    void startMaintenance_shouldReturnOk() throws Exception {

        Long maintenanceId = 1L;

        MaintenanceResponse response = new MaintenanceResponse(
                maintenanceId,
                1L,
                "Leaking tap",
                "Kitchen tap is leaking",
                null, MaintenanceStatus.IN_PROGRESS,
                LocalDate.of(2026, 8, 1),
                null);

        when(maintenanceService.startMaintenance(maintenanceId))
                .thenReturn(response);

        mockMvc.perform(
                patch("/api/maintenance/{maintenanceId}/start", maintenanceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(maintenanceService)
                .startMaintenance(maintenanceId);
    }

    @Test
    void completeMaintenance_shouldReturnOk() throws Exception {

        Long maintenanceId = 1L;

        MaintenanceResponse response = new MaintenanceResponse(
                maintenanceId,
                1L,
                "Leaking tap",
                "Kitchen tap is leaking",
                null, MaintenanceStatus.COMPLETED,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 3));

        when(maintenanceService.completeMaintenance(maintenanceId))
                .thenReturn(response);

        mockMvc.perform(
                patch("/api/maintenance/{maintenanceId}/complete", maintenanceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(maintenanceService)
                .completeMaintenance(maintenanceId);
    }

    @Test
    void deleteMaintenance_shouldReturnNoContent() throws Exception {

        Long maintenanceId = 1L;

        mockMvc.perform(
                delete("/api/maintenance/{maintenanceId}", maintenanceId))
                .andExpect(status().isNoContent());

        verify(maintenanceService)
                .deleteMaintenance(maintenanceId);
    }

}
