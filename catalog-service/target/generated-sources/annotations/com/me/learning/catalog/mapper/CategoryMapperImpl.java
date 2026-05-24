package com.me.learning.catalog.mapper;

import com.me.learning.catalog.dto.CategoryRequestDto;
import com.me.learning.catalog.dto.CategoryResponseDto;
import com.me.learning.catalog.dto.CategorySummaryDto;
import com.me.learning.catalog.entity.Category;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-24T07:43:08+0400",
    comments = "version: 1.6.2, compiler: javac, environment: Java 25.0.2 (Eclipse Adoptium)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category toEntity(CategoryRequestDto categoryRequestDto) {
        if ( categoryRequestDto == null ) {
            return null;
        }

        Category category = new Category();

        if ( categoryRequestDto.id() != null ) {
            category.setId( categoryRequestDto.id() );
        }
        if ( categoryRequestDto.name() != null ) {
            category.setName( categoryRequestDto.name() );
        }
        if ( categoryRequestDto.lastUpdate() != null ) {
            category.setLastUpdate( categoryRequestDto.lastUpdate() );
        }

        return category;
    }

    @Override
    public CategoryResponseDto toResponseDto(Category category) {
        if ( category == null ) {
            return null;
        }

        Short id = null;
        String name = null;
        Instant lastUpdate = null;

        if ( category.getId() != null ) {
            id = category.getId();
        }
        if ( category.getName() != null ) {
            name = category.getName();
        }
        if ( category.getLastUpdate() != null ) {
            lastUpdate = category.getLastUpdate();
        }

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto( id, name, lastUpdate );

        return categoryResponseDto;
    }

    @Override
    public CategorySummaryDto toSummaryDto(Category category) {
        if ( category == null ) {
            return null;
        }

        Short id = null;
        String name = null;

        if ( category.getId() != null ) {
            id = category.getId();
        }
        if ( category.getName() != null ) {
            name = category.getName();
        }

        CategorySummaryDto categorySummaryDto = new CategorySummaryDto( id, name );

        return categorySummaryDto;
    }

    @Override
    public Category partialUpdate(CategoryRequestDto categoryRequestDto, Category category) {
        if ( categoryRequestDto == null ) {
            return category;
        }

        if ( categoryRequestDto.id() != null ) {
            category.setId( categoryRequestDto.id() );
        }
        if ( categoryRequestDto.name() != null ) {
            category.setName( categoryRequestDto.name() );
        }
        if ( categoryRequestDto.lastUpdate() != null ) {
            category.setLastUpdate( categoryRequestDto.lastUpdate() );
        }

        return category;
    }
}
