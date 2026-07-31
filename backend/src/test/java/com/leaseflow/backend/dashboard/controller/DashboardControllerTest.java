package com.leaseflow.backend.dashboard.controller;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leaseflow.backend.dashboard.dto.DashboardResponse;
import com.leaseflow.backend.dashboard.dto.DashboardResponse.NextPaymentResponse;
import com.leaseflow.backend.dashboard.service.DashboardService;
import com.leaseflow.backend.payment.entity.PaymentStatus;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DashboardService dashboardService;

    @Test
    void getDashboard_shouldReturnOk() throws Exception {

        Long userId = 1L;

        NextPaymentResponse nextPayment = new NextPaymentResponse(
                new BigDecimal("580.00"),
                java.time.LocalDate.of(2026, 8, 3),
                PaymentStatus.PENDING);

        DashboardResponse response = new DashboardResponse(
                2L,
                1L,
                new BigDecimal("580.00"),
                nextPayment,
                new BigDecimal("1160.00"),
                1L,
                2L);

        when(dashboardService.getDashboard(userId))
                .thenReturn(response);

        mockMvc.perform(
                get("/api/dashboard/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.propertyCount").value(2))
                .andExpect(jsonPath("$.activeLeaseCount").value(1))
                .andExpect(jsonPath("$.weeklyRent").value(580.00))
                .andExpect(jsonPath("$.outstandingRent").value(1160.00))
                .andExpect(jsonPath("$.overduePayments").value(1))
                .andExpect(jsonPath("$.openMaintenanceRequests").value(2))
                .andExpect(jsonPath("$.nextPayment.amount").value(580.00))
                .andExpect(jsonPath("$.nextPayment.status").value("PENDING"));

        verify(dashboardService)
                .getDashboard(userId);
    }

}
