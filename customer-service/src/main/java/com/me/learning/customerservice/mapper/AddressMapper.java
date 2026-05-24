package com.me.learning.customerservice.mapper;


import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.me.learning.customerservice.dto.AddressRequestDto;
import com.me.learning.customerservice.entity.Address;

/**
 * Maps between {@link Address} and {@link AddressRequestDto}.
 *
 * <p>{@code uses = CityMapper.class} delegates the {@code City ↔ CityRequestDto}
 * conversion to the dedicated mapper, which in turn delegates {@code Country ↔ CountryRequestDto}
 * to {@link CountryMapper}. Without this chain, MapStruct silently drops
 * {@code city} due to the field-name mismatch
 * (entity field {@code city} vs DTO field {@code city}).
 * </p>
 */
@Mapper (unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CityMapper.class})
public interface AddressMapper {

    @Mapping (source = "city", target = "city")
    Address toEntity (AddressRequestDto addressRequestDto);

    @Mapping (source = "city", target = "city")
    AddressRequestDto toDto (Address address);

    @BeanMapping (nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping (source = "city", target = "city")
    Address partialUpdate (AddressRequestDto addressRequestDto, @MappingTarget Address address);
}
