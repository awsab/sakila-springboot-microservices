package com.me.learning.customerservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.me.learning.customerservice.dto.CityRequestDto;
import com.me.learning.customerservice.entity.City;

/**
 * Maps between {@link City} and {@link CityRequestDto}.
 *
 * <p>{@code uses = CountryMapper.class} delegates the {@code Country ↔ CountryRequestDto}
 * conversion to the dedicated mapper, avoiding duplicated conversion logic.
 * Without this, MapStruct cannot resolve the type conversion and silently drops
 * the nested {@code country} field (name mismatch: entity field is
 * {@code country}, DTO field is {@code country}).
 * </p>
 */
@Mapper (unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CountryMapper.class})
public interface CityMapper {

    @Mapping (source = "country", target = "country")
    City toEntity (CityRequestDto cityRequestDto);

    @Mapping (source = "country", target = "country")
    CityRequestDto toDto (City city);

    @BeanMapping (nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping (source = "country", target = "country")
    City partialUpdate (CityRequestDto cityRequestDto, @MappingTarget City city);
}
