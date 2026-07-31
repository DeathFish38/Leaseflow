package com.leaseflow.backend.lease.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

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

import com.leaseflow.backend.lease.dto.CreateLeaseRequest;
import com.leaseflow.backend.lease.dto.LeaseResponse;
import com.leaseflow.backend.lease.dto.UpdateLeaseRequest;
import com.leaseflow.backend.lease.entity.PaymentFrequency;
import com.leaseflow.backend.lease.service.LeaseService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(LeaseController.class)
class LeaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LeaseService leaseService;

    @Test
    void createLease_shouldReturnCreated() throws Exception {

        Long propertyId = 1L;

        CreateLeaseRequest request = new CreateLeaseRequest(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31),
                new BigDecimal("580.00"), null, null, null, null);

        LeaseResponse response = new LeaseResponse(
                1L,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31),
                new BigDecimal("580.00"),
                new BigDecimal("2320.00"),
                PaymentFrequency.WEEKLY,
                "ACTIVE",
                "Test lease");

        when(leaseService.createLease(
                eq(propertyId),
                any(CreateLeaseRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/properties/{propertyId}/leases", propertyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.propertyId").value(1))
                .andExpect(jsonPath("$.weeklyRent").value(580.00));

        verify(leaseService).createLease(
                eq(propertyId),
                any(CreateLeaseRequest.class));
    }

    @Test
    void getLeaseByPropertyId_shouldReturnOk() throws Exception {

        Long propertyId = 1L;

        LeaseResponse response = new LeaseResponse(
                1L,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31),
                new BigDecimal("580.00"),
                new BigDecimal("2320.00"),
                PaymentFrequency.WEEKLY,
                "ACTIVE",
                "Test lease");

        when(leaseService.getLeaseByPropertyId(propertyId))
                .thenReturn(response);

        mockMvc.perform(get("/api/properties/{propertyId}/leases", propertyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.propertyId").value(1));

        verify(leaseService).getLeaseByPropertyId(propertyId);
    }

    @Test
    void updateLease_shouldReturnOk() throws Exception {

        Long leaseId = 1L;

        UpdateLeaseRequest request = new UpdateLeaseRequest(
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 12, 31),
                new BigDecimal("600.00"), null, null, null, null);

        LeaseResponse response = new LeaseResponse(
                1L,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31),
                new BigDecimal("580.00"),
                new BigDecimal("2320.00"),
                PaymentFrequency.WEEKLY,
                "ACTIVE",
                "Test lease");

        when(leaseService.updateLease(
                eq(leaseId),
                any(UpdateLeaseRequest.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/api/leases/{leaseId}", leaseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weeklyRent").value(600.00));

        verify(leaseService).updateLease(
                eq(leaseId),
                any(UpdateLeaseRequest.class));
    }

    @Test
    void deleteLease_shouldReturnNoContent() throws Exception {

        Long leaseId = 1L;

        mockMvc.perform(delete("/api/leases/{leaseId}", leaseId))
                .andExpect(status().isNoContent());

        verify(leaseService).deleteLease(leaseId);
    }

}
