package com.me.learning.customer.customerservice.repository;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.NonNull;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.me.learning.customerservice.entity.Address;

/**
 * Repository for {@link Address}.
 *
 * <p>The {@link EntityGraph} on {@code findAll()} and {@code findById()} eagerly fetches
 * the association chain {@code city → country} in a single JOIN query,
 * preventing the N+1 problem that would otherwise arise because both ManyToOne
 * relationships are {@code FetchType.LAZY} while
 * {@link com.me.learning.customerservice.dto.AddressRequestDto} embeds
 * {@link com.me.learning.customerservice.dto.CityRequestDto} which in turn embeds
 * {@link com.me.learning.customerservice.dto.CountryRequestDto}.
 * </p>
 */
public interface AddressRepository extends JpaRepository<Address, Integer>, JpaSpecificationExecutor<Address> {

    @Override
    @NonNull
    @EntityGraph (attributePaths = {"city", "city.country"})
    List<Address> findAll();

    @Override
    @NonNull
    @EntityGraph (attributePaths = {"city", "city.country"})
    Optional<Address> findById(@NonNull Integer id);
}
