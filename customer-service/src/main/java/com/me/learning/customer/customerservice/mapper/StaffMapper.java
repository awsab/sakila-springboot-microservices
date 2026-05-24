package com.me.learning.customer.customerservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.me.learning.customerservice.dto.StaffRequestDto;
import com.me.learning.customerservice.entity.Staff;

/**
 * Maps between {@link Staff} and {@link StaffRequestDto}.
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
public interface StaffMapper {

    @Mapping (source = "address", target = "address")
    Staff toEntity (StaffRequestDto staffRequestDto);

    @Mapping (source = "address", target = "address")
    StaffRequestDto toDto (Staff staff);

    @BeanMapping (nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping (source = "address", target = "address")
    Staff partialUpdate (StaffRequestDto staffRequestDto, @MappingTarget Staff staff);
}
