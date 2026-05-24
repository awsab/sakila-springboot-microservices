package com.me.learning.customer.customerservice.dto;

import java.io.Serializable;
import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO for {@link com.me.learning.customerservice.entity.Staff}
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public record StaffRequestDto(Short id,
                              @NotNull @Size (max = 45) String firstName,
                              @NotNull @Size (max = 45) String lastName,
                              byte[] picture, @Size (max = 50) String email,
                              @NotNull Boolean active,
                              @NotNull @Size (max = 16) String username,
                              @Size (max = 40) String password,
                              @NotNull Instant lastUpdate,
                              AddressRequestDto address)
        implements Serializable {
}
