package com.leaseflow.backend.common.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.leaseflow.backend.common.exception.lease.InvalidLeaseDateException;
import com.leaseflow.backend.common.exception.lease.LeaseAlreadyExistsException;
import com.leaseflow.backend.common.exception.lease.LeaseNotFoundException;
import com.leaseflow.backend.common.exception.maintenance.InvalidMaintenanceStatusException;
import com.leaseflow.backend.common.exception.maintenance.MaintenanceNotFoundException;
import com.leaseflow.backend.common.exception.payment.InvalidPaymentException;
import com.leaseflow.backend.common.exception.payment.PaymentAlreadyPaidException;
import com.leaseflow.backend.common.exception.payment.PaymentDateException;
import com.leaseflow.backend.common.exception.payment.PaymentGenerationException;
import com.leaseflow.backend.common.exception.payment.PaymentNotFoundException;
import com.leaseflow.backend.common.exception.property.PropertyNotFoundException;
import com.leaseflow.backend.common.exception.user.DuplicateEmailException;
import com.leaseflow.backend.common.exception.user.InvalidCredentialsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        // handle duplicated email for register
        @ExceptionHandler(DuplicateEmailException.class)
        public ResponseEntity<ApiError> handleDuplicateEmail(DuplicateEmailException ex) {

                ApiError error = new ApiError(
                                HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(),
                                ex.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(error);
        }

        // handle login credentials
        @ExceptionHandler(InvalidCredentialsException.class)
        public ResponseEntity<ApiError> handleInvalidCredentials(InvalidCredentialsException ex) {
                ApiError error = new ApiError(
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                ex.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

        // Property management
        @ExceptionHandler(PropertyNotFoundException.class)
        public ResponseEntity<ApiError> PropertyNotFoundException(PropertyNotFoundException ex) {
                ApiError error = new ApiError(
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                ex.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

        // Lease management exceptions
        @ExceptionHandler(InvalidLeaseDateException.class)
        public ResponseEntity<ApiError> InvalidLeaseDateException(InvalidLeaseDateException ex) {
                ApiError error = new ApiError(
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                ex.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

        @ExceptionHandler(LeaseNotFoundException.class)
        public ResponseEntity<ApiError> LeaseNotFoundException(LeaseNotFoundException ex) {
                ApiError error = new ApiError(
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                ex.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

        @ExceptionHandler(LeaseAlreadyExistsException.class)
        public ResponseEntity<ApiError> LeaseAlreadyExistsException(LeaseAlreadyExistsException ex) {
                ApiError error = new ApiError(
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                ex.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

        // Payment
        @ExceptionHandler(PaymentNotFoundException.class)
        public ResponseEntity<ApiError> PaymentNotFoundException(PaymentNotFoundException ex) {
                ApiError error = new ApiError(
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                ex.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

        @ExceptionHandler(InvalidPaymentException.class)
        public ResponseEntity<ApiError> InvalidPaymentException(InvalidPaymentException ex) {
                ApiError error = new ApiError(
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                ex.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

        @ExceptionHandler(PaymentAlreadyPaidException.class)
        public ResponseEntity<ApiError> PaymentAlreadyPaidException(PaymentAlreadyPaidException ex) {
                ApiError error = new ApiError(
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                ex.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

        @ExceptionHandler(PaymentDateException.class)
        public ResponseEntity<ApiError> PaymentDateException(PaymentDateException ex) {
                ApiError error = new ApiError(
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                ex.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

        @ExceptionHandler(PaymentGenerationException.class)
        public ResponseEntity<ApiError> PaymentGenerationException(PaymentGenerationException ex) {
                ApiError error = new ApiError(
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                ex.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

        // maintenance
        @ExceptionHandler(MaintenanceNotFoundException.class)
        public ResponseEntity<ApiError> MaintenanceNotFoundException(MaintenanceNotFoundException ex) {
                ApiError error = new ApiError(
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                ex.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

        @ExceptionHandler(InvalidMaintenanceStatusException.class)
        public ResponseEntity<ApiError> InvalidMaintenanceStatusException(InvalidMaintenanceStatusException ex) {
                ApiError error = new ApiError(
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                ex.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

}
