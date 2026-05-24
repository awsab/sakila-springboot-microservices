package com.me.learning.customerservice.dto;

import java.io.Serializable;
import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO for {@link com.me.learning.customerservice.entity.Customer}
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public record CustomerRequestDto(Integer id,
                                 @NotNull @Size (max = 45) String firstName,
                                 @NotNull @Size (max = 45) String lastName,
                                 @Size (max = 50) String email,
                                 @NotNull Boolean active,
                                 @NotNull Instant createDate,
                                 Instant lastUpdate,
                                 AddressRequestDto address)
        implements Serializable {
}
