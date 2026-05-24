package com.me.learning.customer.customerservice.repository;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.NonNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.me.learning.customerservice.entity.Customer;

/**
 * Repository for {@link Customer}.
 *
 * <p>The {@link EntityGraph} on {@code findAll()} and {@code findById()} eagerly fetches
 * the full association chain {@code address → city → country} in a single JOIN query,
 * preventing the N+1 problem that would otherwise arise because every ManyToOne
 * relationship on these entities is mapped as {@code FetchType.LAZY} while
 * {@link com.me.learning.customerservice.dto.CustomerRequestDto} embeds the full
 * nested DTO hierarchy.
 * </p>
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {

    @Override
    @NonNull
    @EntityGraph (attributePaths = {"address", "address.city", "address.city.country"})
    List<Customer> findAll();

    @Override
    @NonNull
    @EntityGraph (attributePaths = {"address", "address.city", "address.city.country"})
    Page<Customer> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    @EntityGraph (attributePaths = {"address", "address.city", "address.city.country"})
    Optional<Customer> findById(@NonNull Integer id);
}
