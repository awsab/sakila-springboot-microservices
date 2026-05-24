package com.me.learning.customerservice.mapper;


import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.me.learning.customerservice.dto.CountryRequestDto;
import com.me.learning.customerservice.entity.Country;

@Mapper (unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CountryMapper {
    Country toEntity (CountryRequestDto countryRequestDto);

    CountryRequestDto toDto (Country country);

    @BeanMapping (nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Country partialUpdate (
            CountryRequestDto countryRequestDto, @MappingTarget Country country);
}
