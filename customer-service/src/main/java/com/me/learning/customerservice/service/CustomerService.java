package com.me.learning.customerservice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.me.learning.customerservice.dto.CustomerRequestDto;
import com.me.learning.customerservice.dto.CustomerResponseDto;

/**
 * Service interface for {@link com.me.learning.customerservice.entity.Customer} CRUD operations.
 *
 * <p>All read operations are executed in a read-only transaction.
 * Write operations ({@code create}, {@code update}, {@code partialUpdate}, {@code delete})
 * participate in a full read-write transaction.
 *
 * <p>Every method that returns customer data produces a {@link CustomerResponseDto} —
 * a flat representation that includes only the IDs of related entities
 * ({@code addressId}, {@code cityId}, {@code countryId}).
 * {@link CustomerRequestDto} is used exclusively as the inbound write payload.
 * </p>
 */
public interface CustomerService {

    /**
     * Create a new customer.
     *
     * @param dto the customer data; {@code dto.id()} is ignored — the ID is DB-generated
     * @return the persisted customer as a flat {@link CustomerResponseDto}
     */
    CustomerResponseDto create (CustomerRequestDto dto);

    /**
     * Fully replace all fields of an existing customer.
     *
     * @param id  path-variable customer ID
     * @param dto new field values; all non-ID fields are overwritten
     * @return the updated customer as a flat {@link CustomerResponseDto}
     * @throws com.me.learning.framework.web.errors.ResourceNotFoundException if the customer or
     *         the supplied address does not exist
     */
    CustomerResponseDto update (Integer id, CustomerRequestDto dto);

    /**
     * Partially update an existing customer — only non-{@code null} DTO fields are applied.
     *
     * @param id  path-variable customer ID
     * @param dto partial field values; {@code null} fields are left unchanged
     * @return the updated customer as a flat {@link CustomerResponseDto}
     * @throws com.me.learning.framework.web.errors.ResourceNotFoundException if the customer or
     *         the supplied address does not exist
     */
    CustomerResponseDto partialUpdate (Integer id, CustomerRequestDto dto);

    /**
     * Retrieve a single customer by its primary key.
     *
     * @param id customer primary key
     * @return the customer as a flat {@link CustomerResponseDto}
     * @throws com.me.learning.framework.web.errors.ResourceNotFoundException if not found
     */
    CustomerResponseDto findById (Integer id);

    /**
     * Retrieve all customers as an unpaged list.
     *
     * @return list of all customers as flat {@link CustomerResponseDto}s
     */
    List<CustomerResponseDto> findAll ();

    /**
     * Retrieve a page of customers.
     *
     * @param pageable pagination and sort parameters
     * @return a page of customers as flat {@link CustomerResponseDto}s
     */
    Page<CustomerResponseDto> findAll (Pageable pageable);

    /**
     * Delete a customer by ID.
     *
     * @param id customer primary key
     * @throws com.me.learning.framework.web.errors.ResourceNotFoundException if not found
     */
    void delete (Integer id);

    /**
     * Check whether a customer with the given ID exists.
     *
     * @param id customer primary key
     * @return {@code true} if a customer with {@code id} exists
     */
    boolean existsById (Integer id);

    /**
     * Count the total number of customers.
     *
     * @return total count
     */
    long count ();
}

