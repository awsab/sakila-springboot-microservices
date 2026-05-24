package com.me.learning.customerservice.dto;

import java.io.Serializable;
import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO for {@link com.me.learning.customerservice.entity.Country}
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public record CountryRequestDto(Integer id,
                                @NotNull @Size (max = 50) String country,
                                @NotNull Instant lastUpdate) implements Serializable {
}
