package com.me.learning.customer.customerservice.dto;

import java.io.Serializable;
import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO for {@link com.me.learning.customerservice.entity.City}
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public record CityRequestDto(Integer id,
                             @NotNull @Size (max = 50) String city,
                             @NotNull Instant lastUpdate,
                             CountryRequestDto country) implements Serializable {
}
