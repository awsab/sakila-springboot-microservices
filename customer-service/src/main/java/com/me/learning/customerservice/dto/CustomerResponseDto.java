package com.me.learning.customerservice.dto;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Read-model DTO for {@link com.me.learning.customerservice.entity.Customer}.
 *
 * <p>Keeps the payload flat: all Customer scalar fields are included, but the
 * related entities (Address, City, Country) are represented only by their
 * primary-key IDs rather than as full nested objects.
 *
 * <p>This is the canonical response shape returned by every
 * {@code GET}, {@code POST}, {@code PUT}, and {@code PATCH} endpoint.
 * {@link CustomerRequestDto} continues to be used solely as the inbound
 * write payload.
 *
 * <ul>
 *   <li>{@code addressId}  → {@code Customer.address.id}</li>
 *   <li>{@code cityId}     → {@code Customer.address.city.id}</li>
 *   <li>{@code countryId}  → {@code Customer.address.city.country.id}</li>
 * </ul>
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public record CustomerResponseDto(
        Integer id,
        String firstName,
        String lastName,
        String email,
        Boolean active,
        Instant createDate,
        Instant lastUpdate,
        Integer addressId,
        Integer cityId,
        Integer countryId
) implements Serializable {
}

