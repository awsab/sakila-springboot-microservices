package com.me.learning.customerservice.repository;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.NonNull;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.me.learning.customerservice.entity.City;

/**
 * Repository for {@link City}.
 *
 * <p>The {@link EntityGraph} on {@code findAll()} and {@code findById()} eagerly fetches
 * the {@code country} association in a single JOIN query, preventing the N+1 problem
 * that would otherwise arise because {@code City.country} is {@code FetchType.LAZY}
 * while {@link com.me.learning.customerservice.dto.CityRequestDto} embeds
 * {@link com.me.learning.customerservice.dto.CountryRequestDto}.
 * </p>
 */
public interface CityRepository extends JpaRepository<City, Integer>, JpaSpecificationExecutor<City> {

    @Override
    @NonNull
    @EntityGraph (attributePaths = {"country"})
    List<City> findAll();

    @Override
    @NonNull
    @EntityGraph (attributePaths = {"country"})
    Optional<City> findById(@NonNull Integer id);
}
