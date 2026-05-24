package com.me.learning.customer.customerservice.mapper;


import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.me.learning.customerservice.dto.CustomerRequestDto;
import com.me.learning.customerservice.dto.CustomerResponseDto;
import com.me.learning.customerservice.entity.Customer;

/**
 * Maps between {@link Customer} and {@link CustomerRequestDto} / {@link CustomerResponseDto}.
 *
 * <p>{@code uses = AddressMapper.class} delegates the {@code Address ↔ AddressRequestDto}
 * conversion (and its nested {@code City → CityRequestDto → CountryRequestDto} chain)
 * to the dedicated mapper. Without this, MapStruct silently drops the nested
 * {@code address} field due to the field-name mismatch
 * (entity field {@code address} vs DTO field {@code address}).
 * </p>
 */
@Mapper (unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {AddressMapper.class})
public interface CustomerMapper {

    @Mapping (source = "address", target = "address")
    Customer toEntity (CustomerRequestDto customerRequestDto);

    @Mapping (source = "address", target = "address")
    CustomerRequestDto toDto (Customer customer);

    /**
     * Map a {@link Customer} (with its eagerly-loaded address hierarchy) to a flat
     * {@link CustomerResponseDto} that exposes only the IDs of related entities.
     *
     * <p>MapStruct traverses the nested entity graph using dot-notation sources:
     * <ul>
     *   <li>{@code address.id}               → {@code addressId}</li>
     *   <li>{@code address.city.id}          → {@code cityId}</li>
     *   <li>{@code address.city.country.id}  → {@code countryId}</li>
     * </ul>
     * All scalar fields ({@code firstName}, {@code lastName}, etc.) are mapped by
     * name automatically.
     */
    @Mapping (source = "address.id", target = "addressId")
    @Mapping (source = "address.city.id", target = "cityId")
    @Mapping (source = "address.city.country.id", target = "countryId")
    CustomerResponseDto toResponseDto (Customer customer);

    @BeanMapping (nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping (source = "address", target = "address")
    Customer partialUpdate (CustomerRequestDto customerRequestDto, @MappingTarget Customer customer);
}
