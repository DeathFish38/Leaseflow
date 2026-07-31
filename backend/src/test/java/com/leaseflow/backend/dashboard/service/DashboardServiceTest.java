package com.leaseflow.backend.dashboard.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.leaseflow.backend.dashboard.dto.DashboardResponse;
import com.leaseflow.backend.lease.entity.Lease;
import com.leaseflow.backend.lease.repository.LeaseRepository;
import com.leaseflow.backend.maintenance.entity.MaintenanceStatus;
import com.leaseflow.backend.maintenance.repository.MaintenanceRepository;
import com.leaseflow.backend.payment.entity.Payment;
import com.leaseflow.backend.payment.entity.PaymentStatus;
import com.leaseflow.backend.payment.repository.PaymentRepository;
import com.leaseflow.backend.property.repository.PropertyRepository;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private LeaseRepository leaseRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private MaintenanceRepository maintenanceRepository;

    @InjectMocks
    private DashboardService dashboardService;

    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        // No setup required yet.
    }

    @Test
    void getDashboard_shouldReturnCorrectSummary() {

        LocalDate today = LocalDate.now();

        // Properties
        when(propertyRepository.countByOwnerId(userId))
                .thenReturn(2L);

        // Active leases
        when(leaseRepository
                .countByPropertyOwnerIdAndLeaseStartLessThanEqualAndLeaseEndGreaterThanEqual(
                        userId, today, today))
                .thenReturn(1L);

        // Weekly rent
        Lease activeLease = new Lease();
        activeLease.setLeaseStart(today.minusMonths(1));
        activeLease.setLeaseEnd(today.plusMonths(6));
        activeLease.setWeeklyRent(new BigDecimal("580.00"));

        when(leaseRepository.findByPropertyOwnerId(userId))
                .thenReturn(List.of(activeLease));

        // Next payment
        Payment nextPayment = new Payment();
        nextPayment.setAmount(new BigDecimal("580.00"));
        nextPayment.setDueDate(today.plusDays(3));
        nextPayment.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.findNextPayments(userId, today))
                .thenReturn(List.of(nextPayment));

        // Outstanding rent
        when(paymentRepository.calculateOutstandingRent(userId))
                .thenReturn(new BigDecimal("1160.00"));

        // Overdue payments
        when(paymentRepository.countOverduePayments(userId, today))
                .thenReturn(1L);

        // Open maintenance
        when(maintenanceRepository.countByPropertyOwnerIdAndStatusIn(
                userId,
                List.of(
                        MaintenanceStatus.OPEN,
                        MaintenanceStatus.IN_PROGRESS)))
                .thenReturn(2L);

        // Execute
        DashboardResponse response =
                dashboardService.getDashboard(userId);

        // Assert
        assertThat(response.propertyCount())
                .isEqualTo(2L);

        assertThat(response.activeLeaseCount())
                .isEqualTo(1L);

        assertThat(response.weeklyRent())
                .isEqualByComparingTo("580.00");

        assertThat(response.outstandingRent())
                .isEqualByComparingTo("1160.00");

        assertThat(response.overduePayments())
                .isEqualTo(1L);

        assertThat(response.openMaintenanceRequests())
                .isEqualTo(2L);

        assertThat(response.nextPayment())
                .isNotNull();

        assertThat(response.nextPayment().amount())
                .isEqualByComparingTo("580.00");

        assertThat(response.nextPayment().dueDate())
                .isEqualTo(today.plusDays(3));

        assertThat(response.nextPayment().status())
                .isEqualTo(PaymentStatus.PENDING);
    }
}