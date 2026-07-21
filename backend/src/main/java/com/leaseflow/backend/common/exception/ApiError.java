package com.leaseflow.backend.common.exception;

import java.time.LocalDateTime;

public record ApiError(
        int status,
        String error,
        String message,
        LocalDateTime timestamp) {

}
