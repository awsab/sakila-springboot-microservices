package com.me.learning.customer.customerservice.dto;

import java.io.Serializable;
import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.me.learning.customerservice.entity.Address;
import com.me.learning.customerservice.entity.Customer;

/**
 * DTO for {@link Customer}
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public record CustomerUpdateDto(Integer id,
                                @NotNull @Size (max = 45) String firstName,
                                @NotNull @Size (max = 45) String lastName,
                                @Size (max = 50) String email,
                                @NotNull Address address,
                                @NotNull Boolean active,
                                @NotNull Instant createDate,
                                Instant lastUpdate)
        implements Serializable {
}
