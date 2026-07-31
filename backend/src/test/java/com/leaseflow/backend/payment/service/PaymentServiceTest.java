package com.leaseflow.backend.payment.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.leaseflow.backend.common.exception.payment.InvalidPaymentException;
import com.leaseflow.backend.common.exception.payment.PaymentAlreadyPaidException;
import com.leaseflow.backend.common.exception.payment.PaymentNotFoundException;
import com.leaseflow.backend.lease.entity.Lease;
import com.leaseflow.backend.lease.repository.LeaseRepository;
import com.leaseflow.backend.payment.dto.CreatePaymentRequest;
import com.leaseflow.backend.payment.dto.PaymentResponse;
import com.leaseflow.backend.payment.dto.UpdatePaymentRequest;
import com.leaseflow.backend.payment.entity.Payment;
import com.leaseflow.backend.payment.entity.PaymentStatus;
import com.leaseflow.backend.payment.mapper.PaymentMapper;
import com.leaseflow.backend.payment.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private LeaseRepository leaseRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentService paymentService;

    private Lease lease;
    private Payment payment;

    @BeforeEach
    void setUp() {

        lease = new Lease();

        lease.setId(1L);
        lease.setLeaseStart(LocalDate.of(2026, 1, 1));
        lease.setLeaseEnd(LocalDate.of(2026, 12, 31));
        lease.setWeeklyRent(new BigDecimal("580.00"));

        payment = new Payment();

        payment.setId(1L);
        payment.setLease(lease);
        payment.setAmount(new BigDecimal("580.00"));
        payment.setDueDate(LocalDate.now().plusDays(7));
        payment.setStatus(PaymentStatus.PENDING);
    }

    @Test
    void createPayment_shouldCreatePendingPayment() {

        CreatePaymentRequest request =
                mock(CreatePaymentRequest.class);

        when(request.dueDate())
                .thenReturn(LocalDate.now().plusDays(7));

        when(leaseRepository.findById(1L))
                .thenReturn(Optional.of(lease));

        when(paymentMapper.toEntity(request))
                .thenReturn(payment);

        when(paymentRepository.save(payment))
                .thenReturn(payment);

        PaymentResponse response =
                mock(PaymentResponse.class);

        when(paymentMapper.toResponse(payment))
                .thenReturn(response);

        PaymentResponse result =
                paymentService.createPayment(1L, request);

        assertSame(response, result);

        assertEquals(PaymentStatus.PENDING, payment.getStatus());

        verify(paymentRepository).save(payment);
    }

    @Test
    void createPayment_shouldRejectPaymentOutsideLeasePeriod() {

        CreatePaymentRequest request =
                mock(CreatePaymentRequest.class);

        when(request.dueDate())
                .thenReturn(LocalDate.of(2027, 1, 1));

        when(leaseRepository.findById(1L))
                .thenReturn(Optional.of(lease));

        assertThrows(
                InvalidPaymentException.class,
                () -> paymentService.createPayment(1L, request)
        );

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void createPayment_shouldMarkPastPaymentOverdue() {

        CreatePaymentRequest request =
                mock(CreatePaymentRequest.class);

        LocalDate pastDate =
                LocalDate.now().minusDays(1);

        when(request.dueDate())
                .thenReturn(pastDate);

        when(leaseRepository.findById(1L))
                .thenReturn(Optional.of(lease));

        payment.setDueDate(pastDate);

        when(paymentMapper.toEntity(request))
                .thenReturn(payment);

        when(paymentRepository.save(payment))
                .thenReturn(payment);

        when(paymentMapper.toResponse(payment))
                .thenReturn(mock(PaymentResponse.class));

        paymentService.createPayment(1L, request);

        assertEquals(
                PaymentStatus.OVERDUE,
                payment.getStatus()
        );
    }

    @Test
    void getPayments_shouldReturnPayments() {

        when(leaseRepository.findById(1L))
                .thenReturn(Optional.of(lease));

        when(paymentRepository.findByLeaseId(1L))
                .thenReturn(List.of(payment));

        PaymentResponse response =
                mock(PaymentResponse.class);

        when(paymentMapper.toResponse(payment))
                .thenReturn(response);

        List<PaymentResponse> result =
                paymentService.getPayments(1L);

        assertEquals(1, result.size());
        assertSame(response, result.get(0));
    }

    @Test
    void getPaymentById_shouldReturnPayment() {

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        PaymentResponse response =
                mock(PaymentResponse.class);

        when(paymentMapper.toResponse(payment))
                .thenReturn(response);

        PaymentResponse result =
                paymentService.getPaymentById(1L);

        assertSame(response, result);
    }

    @Test
    void getPaymentById_shouldThrowWhenNotFound() {

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                PaymentNotFoundException.class,
                () -> paymentService.getPaymentById(1L)
        );
    }

    @Test
    void updatePayment_shouldRejectPaidPayment() {

        payment.setStatus(PaymentStatus.PAID);

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        UpdatePaymentRequest request =
                mock(UpdatePaymentRequest.class);

        assertThrows(
                InvalidPaymentException.class,
                () -> paymentService.updatePayment(1L, request)
        );

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void markAsPaid_shouldMarkPaymentAsPaid() {

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(payment))
                .thenReturn(payment);

        PaymentResponse response =
                mock(PaymentResponse.class);

        when(paymentMapper.toResponse(payment))
                .thenReturn(response);

        PaymentResponse result =
                paymentService.markAsPaid(1L);

        assertSame(response, result);

        assertEquals(
                PaymentStatus.PAID,
                payment.getStatus()
        );

        assertNotNull(payment.getPaidDate());

        verify(paymentRepository).save(payment);
    }

    @Test
    void markAsPaid_shouldRejectAlreadyPaidPayment() {

        payment.setStatus(PaymentStatus.PAID);

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        assertThrows(
                PaymentAlreadyPaidException.class,
                () -> paymentService.markAsPaid(1L)
        );

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void deletePayment_shouldDeletePayment() {

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        paymentService.deletePayment(1L);

        verify(paymentRepository)
                .delete(payment);
    }

    @Test
    void generatePayments_shouldGenerateWeeklyPayments() {

        paymentService.generatePayments(lease);

        ArgumentCaptor<List<Payment>> captor =
                ArgumentCaptor.forClass(List.class);

        verify(paymentRepository)
                .saveAll(captor.capture());

        List<Payment> generatedPayments =
                captor.getValue();

        assertFalse(generatedPayments.isEmpty());

        assertEquals(
                LocalDate.of(2026, 1, 1),
                generatedPayments.get(0).getDueDate()
        );

        assertEquals(
                new BigDecimal("580.00"),
                generatedPayments.get(0).getAmount()
        );

        assertEquals(
                PaymentStatus.PENDING,
                generatedPayments.get(0).getStatus()
        );
    }
}