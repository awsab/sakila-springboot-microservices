package com.me.learning.customer.customerservice.repository;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.NonNull;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.me.learning.customerservice.entity.Staff;

/**
 * Repository for {@link Staff}.
 *
 * <p>The {@link EntityGraph} on {@code findAll()} and {@code findById()} eagerly fetches
 * the full association chain {@code address → city → country} in a single JOIN query,
 * preventing the N+1 problem that would otherwise arise because every ManyToOne
 * relationship on these entities is mapped as {@code FetchType.LAZY} while
 * {@link com.me.learning.customerservice.dto.StaffRequestDto} embeds the full
 * nested DTO hierarchy.
 * </p>
 */
public interface StaffRepository extends JpaRepository<Staff, Short>, JpaSpecificationExecutor<Staff> {

    @Override
    @NonNull
    @EntityGraph (attributePaths = {"address", "address.city", "address.city.country"})
    List<Staff> findAll();

    @Override
    @NonNull
    @EntityGraph (attributePaths = {"address", "address.city", "address.city.country"})
    Optional<Staff> findById(@NonNull Short id);
}
