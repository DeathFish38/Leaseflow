package com.leaseflow.backend.property.dto;

import java.time.LocalDate;

public record UpdatePropertyRequest(
                String nickname,
                String addressLine1,
                String suburb,
                String state,
                String postcode,
                LocalDate moveInDate,
                LocalDate moveOutDate) {

}
