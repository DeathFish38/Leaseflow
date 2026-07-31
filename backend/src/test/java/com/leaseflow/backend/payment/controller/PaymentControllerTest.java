package com.leaseflow.backend.payment.controller;

import java.math.BigDecimal;
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

import com.leaseflow.backend.payment.dto.CreatePaymentRequest;
import com.leaseflow.backend.payment.dto.PaymentResponse;
import com.leaseflow.backend.payment.dto.UpdatePaymentRequest;
import com.leaseflow.backend.payment.entity.PaymentMethod;
import com.leaseflow.backend.payment.service.PaymentService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    void createPayment_shouldReturnCreated() throws Exception {

        Long leaseId = 1L;

        CreatePaymentRequest request = new CreatePaymentRequest(
                new BigDecimal("580.00"),
                LocalDate.of(2026, 8, 3),
                PaymentMethod.BANK_TRANSFER,
                "REF123",
                "August rent");

        PaymentResponse response = new PaymentResponse(
                1L,
                leaseId,
                new BigDecimal("580.00"),
                LocalDate.of(2026, 8, 3),
                null, null, PaymentMethod.BANK_TRANSFER,
                "REF123",
                "August rent");

        when(paymentService.createPayment(
                eq(leaseId),
                any(CreatePaymentRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/leases/{leaseId}/payments", leaseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.leaseId").value(1))
                .andExpect(jsonPath("$.amount").value(580.00));

        verify(paymentService).createPayment(
                eq(leaseId),
                any(CreatePaymentRequest.class));
    }

    @Test
    void getPayments_shouldReturnOk() throws Exception {

        Long leaseId = 1L;

        PaymentResponse response = new PaymentResponse(
                1L,
                leaseId,
                new BigDecimal("580.00"),
                LocalDate.of(2026, 8, 3),
                null, null, PaymentMethod.BANK_TRANSFER,
                "REF123",
                "August rent");

        when(paymentService.getPayments(leaseId))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/leases/{leaseId}/payments", leaseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(580.00));

        verify(paymentService).getPayments(leaseId);
    }

    @Test
    void getPaymentById_shouldReturnOk() throws Exception {

        Long paymentId = 1L;

        PaymentResponse response = new PaymentResponse(
                paymentId,
                1L,
                new BigDecimal("580.00"),
                LocalDate.of(2026, 8, 3),
                null, null, PaymentMethod.BANK_TRANSFER,
                "REF123",
                "August rent");

        when(paymentService.getPaymentById(paymentId))
                .thenReturn(response);

        mockMvc.perform(get("/api/payments/{paymentId}", paymentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(580.00));

        verify(paymentService).getPaymentById(paymentId);
    }

    @Test
    void updatePayment_shouldReturnOk() throws Exception {

        Long paymentId = 1L;

        UpdatePaymentRequest request = new UpdatePaymentRequest(
                new BigDecimal("600.00"),
                LocalDate.of(2026, 8, 4),
                PaymentMethod.BANK_TRANSFER,
                "UPDATED",
                "Updated payment");

        PaymentResponse response = new PaymentResponse(
                paymentId,
                1L,
                new BigDecimal("600.00"),
                LocalDate.of(2026, 8, 4),
                null, null, PaymentMethod.BANK_TRANSFER,
                "UPDATED",
                "Updated payment");

        when(paymentService.updatePayment(
                eq(paymentId),
                any(UpdatePaymentRequest.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/api/payments/{paymentId}", paymentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(600.00));

        verify(paymentService).updatePayment(
                eq(paymentId),
                any(UpdatePaymentRequest.class));
    }

    @Test
    void markAsPaid_shouldReturnOk() throws Exception {

        Long paymentId = 1L;

        PaymentResponse response = new PaymentResponse(
                paymentId,
                1L,
                new BigDecimal("580.00"),
                LocalDate.of(2026, 8, 3),
                null, null, PaymentMethod.BANK_TRANSFER,
                "REF123",
                "August rent");

        when(paymentService.markAsPaid(paymentId))
                .thenReturn(response);

        mockMvc.perform(patch("/api/payments/{paymentId}/mark-paid", paymentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(paymentService).markAsPaid(paymentId);
    }

    @Test
    void deletePayment_shouldReturnNoContent() throws Exception {

        Long paymentId = 1L;

        mockMvc.perform(delete("/api/payments/{paymentId}", paymentId))
                .andExpect(status().isNoContent());

        verify(paymentService).deletePayment(paymentId);
    }

}
