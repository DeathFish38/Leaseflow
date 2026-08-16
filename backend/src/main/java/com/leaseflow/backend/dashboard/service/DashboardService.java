package com.leaseflow.backend.dashboard.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.leaseflow.backend.auth.service.AuthService;
import com.leaseflow.backend.dashboard.dto.DashboardResponse;
import com.leaseflow.backend.dashboard.dto.DashboardResponse.NextPaymentResponse;
import com.leaseflow.backend.lease.entity.Lease;
import com.leaseflow.backend.lease.repository.LeaseRepository;
import com.leaseflow.backend.maintenance.entity.MaintenanceStatus;
import com.leaseflow.backend.maintenance.repository.MaintenanceRepository;
import com.leaseflow.backend.payment.entity.Payment;
import com.leaseflow.backend.payment.repository.PaymentRepository;
import com.leaseflow.backend.property.repository.PropertyRepository;
import com.leaseflow.backend.users.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

        private final PropertyRepository propertyRepository;
        private final LeaseRepository leaseRepository;
        private final PaymentRepository paymentRepository;
        private final MaintenanceRepository maintenanceRepository;
        private final AuthService authService;

        // get alll information to display on dashboard
        public DashboardResponse getDashboard() {

                User user = authService.getAuthenticatedUser();
                Long userId = user.getId();
                LocalDate today = LocalDate.now();

                // properties
                long propertyCount = propertyRepository.countByOwnerId(userId);

                // active leases
                List<Lease> activeLeases = leaseRepository.findByPropertyOwnerId(userId)
                                .stream()
                                .filter(lease -> !lease.getLeaseStart().isAfter(today)
                                                && !lease.getLeaseEnd().isBefore(today))
                                .toList();

                // count active leases
                long activeLeaseCount = leaseRepository
                                .countByPropertyOwnerIdAndLeaseStartLessThanEqualAndLeaseEndGreaterThanEqual(userId,
                                                today, today);

                // weekly rent
                @SuppressWarnings("null")
                BigDecimal weeklyRent = activeLeases.stream()
                                .map(Lease::getWeeklyRent)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                // upcoming payment
                List<Payment> upcomingPayments = paymentRepository.findNextPayments(userId, today);
                NextPaymentResponse nextPayment = null;
                if (!upcomingPayments.isEmpty()) {
                        Payment payment = upcomingPayments.get(0);
                        nextPayment = new NextPaymentResponse(payment.getAmount(), payment.getDueDate(),
                                        payment.getStatus());
                }

                // outstanding rent
                BigDecimal outstandingRent = paymentRepository.calculateOutstandingRent(userId);

                // overdue payments
                long overduePayments = paymentRepository.countOverduePayments(userId, today);

                // open maintenance
                long openMaintenanceRequests = maintenanceRepository.countByPropertyOwnerIdAndStatusIn(
                                userId,
                                List.of(MaintenanceStatus.OPEN, MaintenanceStatus.IN_PROGRESS));

                return new DashboardResponse(
                                propertyCount,
                                activeLeaseCount,
                                weeklyRent,
                                nextPayment,
                                outstandingRent,
                                overduePayments,
                                openMaintenanceRequests);

        }
}
