package com.leaseflow.backend.lease.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.leaseflow.backend.common.exception.lease.InvalidLeaseDateException;
import com.leaseflow.backend.common.exception.lease.LeaseAlreadyExistsException;
import com.leaseflow.backend.common.exception.lease.LeaseNotFoundException;
import com.leaseflow.backend.common.exception.property.PropertyNotFoundException;
import com.leaseflow.backend.lease.dto.CreateLeaseRequest;
import com.leaseflow.backend.lease.dto.LeaseResponse;
import com.leaseflow.backend.lease.dto.UpdateLeaseRequest;
import com.leaseflow.backend.lease.entity.Lease;
import com.leaseflow.backend.lease.mapper.LeaseMapper;
import com.leaseflow.backend.lease.repository.LeaseRepository;
import com.leaseflow.backend.payment.service.PaymentService;
import com.leaseflow.backend.property.entity.Property;
import com.leaseflow.backend.property.repository.PropertyRepository;

@ExtendWith(MockitoExtension.class)
class LeaseServiceTest {

        @Mock
        private LeaseRepository leaseRepository;

        @Mock
        private PropertyRepository propertyRepository;

        @Mock
        private LeaseMapper leaseMapper;

        @Mock
        private PaymentService paymentService;

        @InjectMocks
        private LeaseService leaseService;

        private Property property;
        private Lease lease;

        @BeforeEach
        void setUp() {
                property = new Property();
                property.setId(1L);

                lease = new Lease();
                lease.setId(1L);
                lease.setProperty(property);
                lease.setLeaseStart(LocalDate.of(2026, 1, 1));
                lease.setLeaseEnd(LocalDate.of(2026, 12, 31));
                lease.setWeeklyRent(new BigDecimal("580.00"));
        }

        @Test
        void createLease_shouldCreateLeaseAndGeneratePayments() {

                CreateLeaseRequest request = mock(CreateLeaseRequest.class);

                when(request.leaseStart())
                                .thenReturn(LocalDate.of(2026, 1, 1));

                when(request.leaseEnd())
                                .thenReturn(LocalDate.of(2026, 12, 31));

                when(propertyRepository.findById(1L))
                                .thenReturn(Optional.of(property));

                when(leaseRepository.findByPropertyId(1L))
                                .thenReturn(Optional.empty());

                when(leaseMapper.toEntity(request))
                                .thenReturn(lease);

                when(leaseRepository.save(lease))
                                .thenReturn(lease);

                LeaseResponse response = mock(LeaseResponse.class);

                when(leaseMapper.toResponse(lease))
                                .thenReturn(response);

                LeaseResponse result = leaseService.createLease(1L, request);

                assertSame(response, result);

                assertEquals(property, lease.getProperty());

                verify(leaseRepository).save(lease);

                verify(paymentService)
                                .generatePayments(lease);
        }

        @Test
        void createLease_shouldThrowWhenPropertyNotFound() {

                CreateLeaseRequest request = mock(CreateLeaseRequest.class);

                when(propertyRepository.findById(1L))
                                .thenReturn(Optional.empty());

                assertThrows(
                                PropertyNotFoundException.class,
                                () -> leaseService.createLease(1L, request));

                verify(leaseRepository, never()).save(any());
                verify(paymentService, never()).generatePayments(any());
        }

        @Test
        void createLease_shouldThrowWhenLeaseAlreadyExists() {

                CreateLeaseRequest request = mock(CreateLeaseRequest.class);

                when(propertyRepository.findById(1L))
                                .thenReturn(Optional.of(property));

                when(leaseRepository.findByPropertyId(1L))
                                .thenReturn(Optional.of(lease));

                assertThrows(
                                LeaseAlreadyExistsException.class,
                                () -> leaseService.createLease(1L, request));

                verify(leaseRepository, never()).save(any());
                verify(paymentService, never()).generatePayments(any());
        }

        @Test
        void createLease_shouldRejectInvalidDates() {

                CreateLeaseRequest request = mock(CreateLeaseRequest.class);

                when(request.leaseStart())
                                .thenReturn(LocalDate.of(2026, 12, 31));

                when(request.leaseEnd())
                                .thenReturn(LocalDate.of(2026, 1, 1));

                when(propertyRepository.findById(1L))
                                .thenReturn(Optional.of(property));

                when(leaseRepository.findByPropertyId(1L))
                                .thenReturn(Optional.empty());

                assertThrows(
                                InvalidLeaseDateException.class,
                                () -> leaseService.createLease(1L, request));

                verify(leaseRepository, never()).save(any());
        }

        @Test
        void getLeaseByPropertyId_shouldReturnLease() {

                when(leaseRepository.findByPropertyId(1L))
                                .thenReturn(Optional.of(lease));

                LeaseResponse response = mock(LeaseResponse.class);

                when(leaseMapper.toResponse(lease))
                                .thenReturn(response);

                LeaseResponse result = leaseService.getLeaseByPropertyId(1L);

                assertSame(response, result);
        }

        @Test
        void getLeaseByPropertyId_shouldThrowWhenNotFound() {

                when(leaseRepository.findByPropertyId(1L))
                                .thenReturn(Optional.empty());

                assertThrows(
                                LeaseNotFoundException.class,
                                () -> leaseService.getLeaseByPropertyId(1L));
        }

        @Test
        void updateLease_shouldUpdateLease() {

                UpdateLeaseRequest request = mock(UpdateLeaseRequest.class);

                when(leaseRepository.findById(1L))
                                .thenReturn(Optional.of(lease));

                when(leaseRepository.save(lease))
                                .thenReturn(lease);

                LeaseResponse response = mock(LeaseResponse.class);

                when(leaseMapper.toResponse(lease))
                                .thenReturn(response);

                LeaseResponse result = leaseService.updateLease(1L, request);

                assertSame(response, result);

                verify(leaseMapper)
                                .updateEntity(lease, request);

                verify(leaseRepository)
                                .save(lease);
        }

        @Test
        void deleteLease_shouldDeleteLease() {

                when(leaseRepository.findById(1L))
                                .thenReturn(Optional.of(lease));

                leaseService.deleteLease(1L);

                verify(leaseRepository)
                                .delete(lease);
        }
}