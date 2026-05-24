package com.me.learning.customerservice.dto;

import java.io.Serializable;
import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO for {@link com.me.learning.customerservice.entity.Address}
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public record AddressRequestDto(Integer id,
                                @NotNull @Size (max = 50) String address,
                                @Size (max = 50) String address2,
                                @NotNull @Size (max = 20) String district,
                                @Size (max = 10) String postalCode,
                                @NotNull @Size (max = 20) String phone,
                                @NotNull Instant lastUpdate,
                                CityRequestDto city)
        implements Serializable {
}
